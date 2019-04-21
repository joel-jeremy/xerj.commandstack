package io.github.xerprojects.xerj.commandstack.entities;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import io.github.xerprojects.xerj.commandstack.Command;
import io.github.xerprojects.xerj.commandstack.CommandHandler;

public abstract class BaseCommandHandler<TCommand extends Command> implements CommandHandler<TCommand> {
	private static final CompletableFuture<Void> COMPLETED_FUTURE = CompletableFuture.completedFuture(null);
	private ArrayList<TCommand> handledCommands = new ArrayList<>();
	
	@Override
	public CompletableFuture<Void> handle(TCommand command) {
		handledCommands.add(command);
		return COMPLETED_FUTURE;
	}
	
	public boolean hasHandledCommand(Command command) {
		// Instance comparison.
		return handledCommands.stream().anyMatch(c -> c == command);
	}
}