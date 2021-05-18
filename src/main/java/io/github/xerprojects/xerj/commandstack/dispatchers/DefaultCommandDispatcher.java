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

package io.github.xerprojects.xerj.commandstack.dispatchers;

import static io.github.xerprojects.xerj.commandstack.utils.Arguments.requireNonNull;

import java.util.Optional;
import java.util.function.Consumer;

import io.github.xerprojects.xerj.commandstack.CommandDispatcher;
import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.CommandHandlerProvider;
import io.github.xerprojects.xerj.commandstack.exceptions.CommandStackException;

/**
 * Default command dispatcher. This dispatcher gets its command handler instances
 * from a {@link CommandHandlerProvider} based on the type of command being dispatched.
 * 
 * @author Joel Jeremy Marquez
 */
public class DefaultCommandDispatcher implements CommandDispatcher {
	
	private final CommandHandlerProvider commandHandlerProvider;
	private final Consumer<Class<?>> defaultCommandHandler;

	/**
	 * Constructor.
	 * @param commandHandlerProvider 
	 * Command handler provider where this dispatcher will get its command handlers from.
	 */
	public DefaultCommandDispatcher(CommandHandlerProvider commandHandlerProvider) {
		// No-op command handler not found handler.
		this(commandHandlerProvider, DefaultCommandDispatcher::noOp);
	}

	/**
	 * Constructor.
	 * @param commandHandlerProvider 
	 * Command handler provider where this dispatcher will get its command handlers from.
	 * @param commandHandlerNotFoundHandler 
	 * This gets executed if no command handler is found for the command being dispatched.
	 * The command handler's Class<?> parameter is the type of the command being dispatched.
	 */
	public DefaultCommandDispatcher(
			CommandHandlerProvider commandHandlerProvider,
			Consumer<Class<?>> commandHandlerNotFoundHandler) {
		this.commandHandlerProvider = 
			new NonNullOptionalProvider(
				requireNonNull(commandHandlerProvider, "commandHandlerProvider"));
		
		this.defaultCommandHandler = 
			requireNonNull(commandHandlerNotFoundHandler, "commandHandlerNotFoundHandler");
	}
	
	/**
	 * Dispatch command to its registered command handler.
	 * This will log a warning message if no command
	 * handler is registered for the given command.
	 * 
	 * @param <TCommand> The command type.
	 * @param command The command to dispatch.
	 */
	public <TCommand> void send(TCommand command) {
		
		requireNonNull(command, "command");
		
		@SuppressWarnings("unchecked")
		Class<TCommand> actualCommandType = (Class<TCommand>)command.getClass();
		
		CommandHandler<TCommand> resolvedHandler = 
			commandHandlerProvider.getCommandHandlerFor(actualCommandType)
				.orElse(c -> defaultCommandHandler.accept(c.getClass()));
		
		resolvedHandler.handle(command);
	}

	private static void noOp(Class<?> commandType) {
		// No-op.
	}

	private static class NonNullOptionalProvider implements CommandHandlerProvider {

		private final CommandHandlerProvider commandHandlerProvider;

		public NonNullOptionalProvider(CommandHandlerProvider commandHandlerProvider) {
			this.commandHandlerProvider = commandHandlerProvider;
		}

		@Override
		public <TCommand> Optional<CommandHandler<TCommand>> getCommandHandlerFor(
				Class<TCommand> commandType) {
			Optional<CommandHandler<TCommand>> handler = 
				commandHandlerProvider.getCommandHandlerFor(commandType);
			
			if (handler == null) {
				throw new CommandStackException(
					"Command handler provider returned null. Please check command handler provider configuration: " +
						commandHandlerProvider.getClass());
			}

			return handler;
		}
	}
}
