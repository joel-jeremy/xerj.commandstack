/**
 * Copyright 2021 Joel Jeremy Marquez
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.xerprojects.xerj.commandstack.exceptions;

/**
 * This is usually thrown if there is no command handler registered for a given command type.
 * 
 * @author Joel Jeremy Marquez
 */
public class CommandHandlerNotFoundException extends CommandStackException {
	
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_EXCEPTION_MESSAGE = "Unable to find registered command handler for command of type %s.";
	
	/**
	 * The command type associated with this exception.
	 */
	private final Class<?> commandType;

	/**
	 * Constructor.
	 * @param commandType The command type.
	 * @param message The exception message.
	 * @param cause The cause of the exception.
	 */
	public CommandHandlerNotFoundException(Class<?> commandType, String message, Throwable cause) {
		super(buildExceptionMessage(commandType, message), cause);
		this.commandType = commandType;
	}

	/**
	 * Constructor.
	 * @param commandType The command type.
	 * @param message The exception message.
	 */
	public CommandHandlerNotFoundException(Class<?> commandType, String message) {
		this(commandType, message, null);
	}
	
	/**
	 * Constructor.
	 * @param commandType The command type.
	 * @param cause The cause of the exception.
	 */
	public CommandHandlerNotFoundException(Class<?> commandType, Throwable cause) {
		this(commandType, null, cause);
	}
	
	/**
	 * Constructor.
	 * @param commandType The command type.
	 */
	public CommandHandlerNotFoundException(Class<?> commandType) {
		this(commandType, null, null);
	}

	/**
	 * The dispatched command that caused the exception.
	 * @return The command type.
	 */
	public Class<?> getCommandType() {
		return commandType;
	}
	
	private static final String buildExceptionMessage(Class<?> commandType, String message) {
		
		if (message == null || message.isBlank()) {
			message = String.format(DEFAULT_EXCEPTION_MESSAGE, commandType);
		}
		
		return message;
	}
}
