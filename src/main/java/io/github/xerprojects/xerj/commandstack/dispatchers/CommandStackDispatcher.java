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

import static io.github.xerprojects.xerj.commandstack.internal.utils.Arguments.requireNonNull;

import java.util.Optional;

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
public class CommandStackDispatcher implements CommandDispatcher {
	
	private final CommandHandlerProvider commandHandlerProvider;
	private final UnhandleCommandListener unhandledCommandListener;

	/**
	 * Constructor.
	 * @param commandHandlerProvider 
	 * Command handler provider where this dispatcher will get its command handlers from.
	 */
	public CommandStackDispatcher(CommandHandlerProvider commandHandlerProvider) {
		// No-op command handler not found handler.
		this(commandHandlerProvider, new NoOpListener());
	}

	/**
	 * Constructor.
	 * @param commandHandlerProvider 
	 * Command handler provider where this dispatcher will get its command handlers from.
	 * @param unhandledCommandListener 
	 * This listener gets executed whenever a command goes unhandled because there was 
	 * no registered command handler.
	 */
	public CommandStackDispatcher(
			CommandHandlerProvider commandHandlerProvider,
			UnhandleCommandListener unhandledCommandListener) {
		this.commandHandlerProvider = 
			new NonNullOptionalProvider(
				requireNonNull(commandHandlerProvider, "commandHandlerProvider"));
		
		this.unhandledCommandListener = 
			requireNonNull(unhandledCommandListener, "unhandledCommandListener");
	}
	
	/**
	 * Dispatch command to its registered command handler.
	 * 
	 * This will invoke the dispatcher's unhandled command listener
	 * when there is no registered command handler for the command.
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
				.orElse(unhandledCommandListener::notifyUnhandledCommand);
		
		resolvedHandler.handle(command);
	}

	/**
	 * Listener that is invoked by {@link CommandStackDispatcher}
	 * whenever a command goes unhandled because there was no registered command handler.
	 */
	public interface UnhandleCommandListener {
		/**
		 * Notify that the command was not dispatched to any command handlers.
		 * 
		 * @param <TCommand> The command type.
		 * @param command The command that was not dispatched to any command handlers.
		 */
		<TCommand> void notifyUnhandledCommand(TCommand command);
	}

	 /**
	  * Decorator that checks if decorated command handler provider's response is null.
	  * If so, throw a {@link CommandStackException}.
	  */
	private static class NonNullOptionalProvider implements CommandHandlerProvider {

		private final CommandHandlerProvider commandHandlerProvider;

		public NonNullOptionalProvider(CommandHandlerProvider commandHandlerProvider) {
			this.commandHandlerProvider = commandHandlerProvider;
		}

		@Override
		public <TCommand> Optional<CommandHandler<TCommand>> getCommandHandlerFor(
				Class<TCommand> commandType) {
			Optional<CommandHandler<TCommand>> resolvedHandler = 
				commandHandlerProvider.getCommandHandlerFor(commandType);
			
			if (resolvedHandler == null) {
				throw new CommandStackException(
					"Command handler provider returned null. Please check command handler provider configuration: " +
						commandHandlerProvider.getClass());
			}

			return resolvedHandler;
		}
	}

	/**
	 * No-op unhandled command listener.
	 */
	private static class NoOpListener implements UnhandleCommandListener {
		@Override
		public <TCommand> void notifyUnhandledCommand(TCommand command) {}
	}
}
