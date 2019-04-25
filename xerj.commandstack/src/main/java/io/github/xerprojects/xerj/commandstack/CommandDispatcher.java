package io.github.xerprojects.xerj.commandstack;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import io.github.xerprojects.xerj.commandstack.exceptions.CommandHandlerNotFoundException;

public class CommandDispatcher {
	
	private final CommandHandlerProvider provider;

	public CommandDispatcher(CommandHandlerProvider provider) {
		if (provider == null) {
			throw new IllegalArgumentException("Command handler provider must not be null.");
		}

		this.provider = provider;
	}
	
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
