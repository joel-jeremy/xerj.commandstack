package io.github.xerprojects.xerj.commandstack.exceptions;

/**
 * Base exception for command stack library.
 * 
 * @author Joel Jeremy Marquez
 */
public class CommandStackException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

    /**
	 * Constructor.
	 * @param message The exception message.
	 */
    public CommandStackException(String message) {
        super(message);
    }

    /**
	 * Constructor.
	 * @param message The exception message.
	 * @param cause The cause of the exception.
	 */
    public CommandStackException(String message, Throwable cause) {
        super(message, cause);
    }
}
