package com.xerprojects.xerj.commandstack;

public interface CommandHandler<TCommand extends Command> {
	void handle(TCommand command);
}
