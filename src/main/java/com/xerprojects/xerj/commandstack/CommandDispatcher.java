package com.xerprojects.xerj.commandstack;

import java.util.ArrayList;
import java.util.Optional;

import com.xerprojects.xerj.commandstack.exceptions.CommandHandlerNotFoundException;
import com.xerprojects.xerj.commandstack.providers.CompositeCommandHandlerProvider;

public interface CommandDispatcher {
	<TCommand extends Command> void send(TCommand command);
	
	static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder {
		public final ArrayList<CommandHandlerProvider> providers = new ArrayList<>();
		
		private Builder() { }
		
		public Builder addCommandHandlerProvider(CommandHandlerProvider provider) {
			if (provider == null) {
				throw new IllegalArgumentException("Provider must not be null.");
			}
			
			providers.add(provider);
			return this;
		}
		
		public CommandDispatcher build() {

			int providersCount = providers.size();
			
			if (providersCount == 1) {
				return new InternalCommandDelegator(providers.get(0));
			}
			
			if (providersCount == 0) {
				return InternalCommandDelegator.NULL;
			}
			
			return new InternalCommandDelegator(
					new CompositeCommandHandlerProvider(providers));
		}
		
		private static final class InternalCommandDelegator implements CommandDispatcher {
			
			static final InternalCommandDelegator NULL = new InternalCommandDelegator(InternalCommandDelegator::nullProvider);

			private final CommandHandlerProvider provider;

			public InternalCommandDelegator(CommandHandlerProvider provider) {
				this.provider = provider;
			}
			
			@Override
			public <TCommand extends Command> void send(TCommand command) {
				
				if (command == null) {
					throw new IllegalArgumentException("Command must not be null.");
				}
				
				@SuppressWarnings("unchecked")
				Class<TCommand> genericClass = (Class<TCommand>) command.getClass();
				Optional<CommandHandler<TCommand>> resolvedHandler = provider.getCommandHandlerFor(genericClass);
				
				if (resolvedHandler.isEmpty()) {
					throw new CommandHandlerNotFoundException(genericClass);
				}
				
				resolvedHandler.get().handle(command);
			}
			
			public static <TCommand extends Command> Optional<CommandHandler<TCommand>> nullProvider(
					Class<TCommand> commandType) {				
				return Optional.empty();
			}
			
		}
		
	}
}
