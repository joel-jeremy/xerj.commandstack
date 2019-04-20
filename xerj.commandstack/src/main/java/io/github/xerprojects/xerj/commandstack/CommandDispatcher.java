package io.github.xerprojects.xerj.commandstack;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import io.github.xerprojects.xerj.commandstack.exceptions.CommandHandlerNotFoundException;
import io.github.xerprojects.xerj.commandstack.providers.CompositeCommandHandlerProvider;

public interface CommandDispatcher {
	<TCommand extends Command> CompletableFuture<Void> send(TCommand command);
	
	static Builder builder() {
		return new Builder();
	}
	
	public static final class Builder {

		private static final CommandDispatcher NULL_DISPATCHER = new InternalCommandDispatcher(Builder::nullProvider);

		private final ArrayList<CommandHandlerProvider> providers = new ArrayList<>();
		
		private Builder() { }
		
		public Builder addCommandHandlerProvider(CommandHandlerProvider provider) {
			if (provider == null) {
				throw new IllegalArgumentException("Command handler provider must not be null.");
			}
			
			providers.add(provider);
			return this;
		}
		
		public CommandDispatcher build() {

			int providersCount = providers.size();
			
			if (providersCount == 1) {
				return new InternalCommandDispatcher(providers.get(0));
			}
			
			if (providersCount == 0) {
				return NULL_DISPATCHER;
			}
			
			return new InternalCommandDispatcher(new CompositeCommandHandlerProvider(providers));
		}
		
		private static final class InternalCommandDispatcher implements CommandDispatcher {

			private final CommandHandlerProvider provider;

			public InternalCommandDispatcher(CommandHandlerProvider provider) {

				if (provider == null) {
					throw new IllegalArgumentException("Command handler provider must not be null.");
				}

				this.provider = provider;
			}
			
			@Override
			public <TCommand extends Command> CompletableFuture<Void> send(TCommand command) {
				
				if (command == null) {
					throw new IllegalArgumentException("Command must not be null.");
				}
				
				@SuppressWarnings("unchecked")
				Class<TCommand> actualCommandType = (Class<TCommand>) command.getClass();
				Optional<CommandHandler<TCommand>> resolvedHandler = provider.getCommandHandlerFor(actualCommandType);
				
				if (!resolvedHandler.isPresent()) {
					throw new CommandHandlerNotFoundException(actualCommandType);
				}
				
				return resolvedHandler.get().handle(command);
			}
		}
			
		private static <TCommand extends Command> Optional<CommandHandler<TCommand>> nullProvider(
				Class<TCommand> commandType) {				
			return Optional.empty();
		}
		
	}
}
