package io.github.xerprojects.xerj.commandstack.testentities;

import java.util.ArrayList;

import io.github.xerprojects.xerj.commandstack.CommandHandler;

public abstract class BaseCommandHandler<TCommand> implements CommandHandler<TCommand> {
	private ArrayList<TCommand> handledCommands = new ArrayList<>();
	
	@Override
	public void handle(TCommand command) {
		handledCommands.add(command);
	}
	
	public <T> boolean hasHandledCommand(T command) {
		// Instance comparison.
		return handledCommands.stream().anyMatch(c -> c == command);
	}
}