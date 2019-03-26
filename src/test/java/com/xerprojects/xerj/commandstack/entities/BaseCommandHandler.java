package com.xerprojects.xerj.commandstack.entities;

import java.util.ArrayList;

import com.xerprojects.xerj.commandstack.Command;
import com.xerprojects.xerj.commandstack.CommandHandler;

public class BaseCommandHandler<TCommand extends Command> implements CommandHandler<TCommand> {
	private ArrayList<TCommand> handledCommands = new ArrayList<>();
	
	@Override
	public void handle(TCommand command) {
		handledCommands.add(command);
	}
	
	public boolean hasHandledCommand(Command command) {
		// Instance comparison.
		return handledCommands.stream().anyMatch(c -> c == command);
	}
}