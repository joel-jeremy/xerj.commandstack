package io.github.xerprojects.xerj.commandstack.exceptions;

import io.github.xerprojects.xerj.commandstack.Command;

public class DuplicateCommandHandlerFoundException extends RuntimeException {

	private static final long serialVersionUID = 1856796726215848487L;
	private static final String DEFAULT_EXCEPTION_MESSAGE = "A duplicate command handler was registered for %s.";
	
	private final Class<? extends Command> commandType;
	
	public DuplicateCommandHandlerFoundException(Class<? extends Command> commandType, String message, Throwable cause) {
		super(message, cause);
		this.commandType = commandType;
	}
	
	public DuplicateCommandHandlerFoundException(Class<? extends Command> commandType, String message) {
		this(commandType, buildExceptionMessage(commandType, message), null);
	}
	
	public DuplicateCommandHandlerFoundException(Class<? extends Command> commandType) {
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
