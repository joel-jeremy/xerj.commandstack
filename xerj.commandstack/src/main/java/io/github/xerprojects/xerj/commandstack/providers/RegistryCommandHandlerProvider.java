package io.github.xerprojects.xerj.commandstack.providers;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

import io.github.xerprojects.xerj.commandstack.Command;
import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.CommandHandlerProvider;
import io.github.xerprojects.xerj.commandstack.exceptions.DuplicateCommandHandlerRegistrationException;

public class RegistryCommandHandlerProvider implements CommandHandlerProvider {

	private final MapRegistry registry;

	public RegistryCommandHandlerProvider(Consumer<Registry> registryConfiguration) {

		if (registryConfiguration == null) {
			throw new IllegalArgumentException("Registry configuration must not be null.");
		}

		registry = new MapRegistry();
		registryConfiguration.accept(registry);
	}

	@Override
	public <TCommand extends Command> Optional<CommandHandler<TCommand>> getCommandHandlerFor(
			Class<TCommand> commandType) {

		if (commandType == null) {
			throw new IllegalArgumentException("Command type must not be null.");
		}

		@SuppressWarnings("unchecked")
		RegisteredCommandHandler<TCommand> registered = (RegisteredCommandHandler<TCommand>) registry.get(commandType);

		if (registered == null) {
			return Optional.empty();
		}

		return Optional.ofNullable(registered.getInstance());
	}

	public static interface Registry {
		<TCommand extends Command> Registry registerCommandHandler(
				Class<TCommand> commandType,
				CommandHandlerInstanceFactory<TCommand> instanceFactory);
	}

	public static interface CommandHandlerInstanceFactory<TCommand extends Command> {
		CommandHandler<TCommand> getInstance();
	}

	private static final class MapRegistry 
			extends HashMap<Class<? extends Command>, RegisteredCommandHandler<? extends Command>>
			implements Registry {

		private static final long serialVersionUID = 7167121127221962355L;

		@Override
		public <TCommand extends Command> Registry registerCommandHandler(
				Class<TCommand> commandType,
				CommandHandlerInstanceFactory<TCommand> instanceFactory) {

			if (commandType == null) {
				throw new IllegalArgumentException("Command type must not be null.");
			}

			if (instanceFactory == null) {
				throw new IllegalArgumentException("Instance factory must not be null.");
			}

			if (containsKey(commandType)) {
				throw new DuplicateCommandHandlerRegistrationException(commandType);
			}

			put(commandType, new RegisteredCommandHandler<>(commandType, instanceFactory));

			return this;
		}
	}
	
	private static final class RegisteredCommandHandler<TCommand extends Command> {
		
		private final Class<TCommand> commandType;
		private final CommandHandlerInstanceFactory<TCommand> instanceFactory;
		 
		public RegisteredCommandHandler(Class<TCommand> commandType, CommandHandlerInstanceFactory<TCommand> instanceFactory) {
			this.commandType = commandType;
			this.instanceFactory = instanceFactory;
		}

		public Class<TCommand> getCommandType() {
			return commandType;
		}
		
		public CommandHandler<TCommand> getInstance() {
			try {
				CommandHandler<TCommand> instance = instanceFactory.getInstance();
				
				if (instance == null) {
					throw new IllegalStateException("Registered command handler instance factory for " + getCommandType() 
						+ " supplied a null instance. Please check configuration.");
				}
				
				return instance;
			} catch (Exception e) {
				throw new IllegalStateException("Registered command handler instance factory for " + getCommandType() 
					+ " has thrown an exception. Please check configuration.", e);
			}
		}
	}
}
