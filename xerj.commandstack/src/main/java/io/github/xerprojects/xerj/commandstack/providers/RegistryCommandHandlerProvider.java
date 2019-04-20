package io.github.xerprojects.xerj.commandstack.providers;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
		RegisteredCommandHandler<TCommand> registered = 
			(RegisteredCommandHandler<TCommand>) registry.get(commandType);

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

	private static final class MapRegistry implements Registry, Map<Class<? extends Command>, RegisteredCommandHandler<? extends Command>> {
		private final LinkedHashMap<Class<? extends Command>, RegisteredCommandHandler<? extends Command>> registeredCommandHandlersByType 
			= new LinkedHashMap<>();

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

			if (registeredCommandHandlersByType.containsKey(commandType)) {
				throw new DuplicateCommandHandlerRegistrationException(commandType);
			}

			registeredCommandHandlersByType.put(commandType,
					new RegisteredCommandHandler<>(commandType, instanceFactory));

			return this;
		}

		@Override
		public int size() {
			return registeredCommandHandlersByType.size();
		}

		@Override
		public boolean isEmpty() {
			return registeredCommandHandlersByType.isEmpty();
		}

		@Override
		public boolean containsKey(Object key) {
			return registeredCommandHandlersByType.containsKey(key);
		}

		@Override
		public boolean containsValue(Object value) {
			return registeredCommandHandlersByType.containsValue(value);
		}

		@Override
		public RegisteredCommandHandler<? extends Command> get(Object key) {
			return registeredCommandHandlersByType.get(key);
		}

		@Override
		public RegisteredCommandHandler<? extends Command> put(
				Class<? extends Command> key,
				RegisteredCommandHandler<? extends Command> value) {
			return registeredCommandHandlersByType.put(key, value);
		}

		@Override
		public RegisteredCommandHandler<? extends Command> remove(Object key) {
			return registeredCommandHandlersByType.remove(key);
		}

		@Override
		public void putAll(
				Map<? extends Class<? extends Command>, ? extends RegisteredCommandHandler<? extends Command>> all) {
			registeredCommandHandlersByType.putAll(all);
		}

		@Override
		public void clear() {
			registeredCommandHandlersByType.clear();
		}

		@Override
		public Set<Class<? extends Command>> keySet() {
			return registeredCommandHandlersByType.keySet();
		}

		@Override
		public Collection<RegisteredCommandHandler<? extends Command>> values() {
			return registeredCommandHandlersByType.values();
		}

		@Override
		public Set<Entry<Class<? extends Command>, RegisteredCommandHandler<? extends Command>>> entrySet() {
			return registeredCommandHandlersByType.entrySet();
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
