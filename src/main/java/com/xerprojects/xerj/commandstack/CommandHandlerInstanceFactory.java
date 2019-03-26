package com.xerprojects.xerj.commandstack;

public interface CommandHandlerInstanceFactory<TCommand extends Command> {
	CommandHandler<TCommand> getInstance();
}
