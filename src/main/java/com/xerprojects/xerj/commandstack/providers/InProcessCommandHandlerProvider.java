package com.xerprojects.xerj.commandstack.providers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.function.Consumer;

import com.xerprojects.xerj.commandstack.Command;
import com.xerprojects.xerj.commandstack.CommandHandler;
import com.xerprojects.xerj.commandstack.CommandHandlerInstanceFactory;
import com.xerprojects.xerj.commandstack.CommandHandlerProvider;
import com.xerprojects.xerj.commandstack.exceptions.DuplicateCommandHandlerRegistrationException;

public class InProcessCommandHandlerProvider implements CommandHandlerProvider {
	
	private final LinkedHashMap<Class<? extends Command>, RegisteredCommandHandler<? extends Command>> commandHandlersByCommandType 
		= new LinkedHashMap<>();
	
	public InProcessCommandHandlerProvider(Consumer<Config> configuration) {
		
		if (configuration == null) {
			throw new IllegalArgumentException("Configuration must not be null.");
		}
		
		InProcessConfig config = new InProcessConfig();
		configuration.accept(config);
		
		// Convert to map for faster lookups.
		for (RegisteredCommandHandler<? extends Command> handler : config) {	
			commandHandlersByCommandType.put(handler.getCommandType(), handler);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <TCommand extends Command> Optional<CommandHandler<TCommand>> getCommandHandlerFor(Class<TCommand> commandType) {
		RegisteredCommandHandler<TCommand> registered = (RegisteredCommandHandler<TCommand>)commandHandlersByCommandType.get(commandType);
		
		if (registered == null) {
			return Optional.empty();
		}
		
		return Optional.ofNullable(registered.getInstance());
	}
	
	public static interface Config {
		<TCommand extends Command> Config registerCommandHandler(
				Class<TCommand> commandType, 
				CommandHandlerInstanceFactory<TCommand> instanceFactory);
	}
	
	private static final class InProcessConfig implements Config, Iterable<RegisteredCommandHandler<? extends Command>> {
		private final ArrayList<RegisteredCommandHandler<? extends Command>> registeredCommandHandlersByType = new ArrayList<>();
		
		@Override
		public <TCommand extends Command> Config registerCommandHandler(
				Class<TCommand> commandType, 
				CommandHandlerInstanceFactory<TCommand> instanceFactory) {
			
			if (commandType == null) {
				throw new IllegalArgumentException("Command type must not be null.");
			}
			
			if (instanceFactory == null) {
				throw new IllegalArgumentException("Instance factory must not be null.");
			}
			
			if (registeredCommandHandlersByType.stream().anyMatch(r -> r.canHandle(commandType))) {
				throw new DuplicateCommandHandlerRegistrationException(commandType);
			}
			
			registeredCommandHandlersByType.add(new RegisteredCommandHandler<>(commandType, instanceFactory));
			return this;
			
		}

		@Override
		public Iterator<RegisteredCommandHandler<? extends Command>> iterator() {
			return registeredCommandHandlersByType.iterator();
		}
	}
	
	private static final class RegisteredCommandHandler<TCommand extends Command> {
		
		private final Class<TCommand> commandType;
		private final CommandHandlerInstanceFactory<TCommand> instanceFactory;
		 
		public RegisteredCommandHandler(Class<TCommand> commandType, CommandHandlerInstanceFactory<TCommand> instanceFactory) {
			this.commandType = commandType;
			this.instanceFactory = instanceFactory;
		}
		
		public boolean canHandle(Class<?> commandType) {
			return getCommandType().equals(commandType);
		}

		public Class<TCommand> getCommandType() {
			return commandType;
		}
		
		public CommandHandler<TCommand> getInstance() {
			try {
				CommandHandler<TCommand> instance = instanceFactory.getInstance();
				
				if (instance == null) {
					throw new IllegalStateException("Registered command handler instance factory for " + commandType + " supplied a null instance.");
				}
				
				return instance;
			} catch (Exception e) {
				throw new IllegalStateException("Registered command handler instance factory for " + commandType + " has thrown an exception.");
			}
		}

		/*@SuppressWarnings("unchecked")
		@Override
		public void handle(Command command) {
			
			if (command == null) {
				throw new IllegalArgumentException("Command must not be null.");
			}
			
			if (!command.getClass().equals(commandType)) {
				throw new IllegalArgumentException("Command handler expects to handle a command of type " + commandType
						+ " but " + command.getClass() + " was provided.");
			}
			
			CommandHandler<TCommand> commandHandler = getInstance();
			
			commandHandler.handle((TCommand)command);		
		}*/
	}

}
