package io.github.xerprojects.xerj.commandstack.exceptions;

import io.github.xerprojects.xerj.commandstack.Command;

public class CommandHandlerNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -3957858332490835079L;
	private static final String DEFAULT_EXCEPTION_MESSAGE = "Unable to find registered command handler for command of type %s.";
	
	private final Class<? extends Command> commandType;

	public CommandHandlerNotFoundException(Class<? extends Command> commandType, String message, Throwable cause) {
		super(message, cause);
		this.commandType = commandType;
	}
	
	public CommandHandlerNotFoundException(Class<? extends Command> commandType, Throwable cause) {
		this(commandType, buildExceptionMessage(commandType, DEFAULT_EXCEPTION_MESSAGE), null);
	}
	
	public CommandHandlerNotFoundException(Class<? extends Command> commandType) {
		this(commandType, null);
	}

	public Class<? extends Command> getCommandType() {
		return commandType;
	}
	
	private static final String buildExceptionMessage(Class<? extends Command> commandType, String message) {
		
		if (message == null || message.isBlank()) {
			message = String.format(DEFAULT_EXCEPTION_MESSAGE, commandType);
		}
		
		return message;
	}
}
