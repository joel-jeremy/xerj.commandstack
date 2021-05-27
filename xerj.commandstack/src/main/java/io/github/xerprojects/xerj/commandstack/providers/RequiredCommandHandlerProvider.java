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

package io.github.xerprojects.xerj.commandstack.providers;

import static io.github.xerprojects.xerj.commandstack.internal.utils.Arguments.requireNonNull;

import java.util.Optional;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.CommandHandlerProvider;
import io.github.xerprojects.xerj.commandstack.exceptions.CommandHandlerNotFoundException;

/**
 * Command handler provider decorator that throws a {@link CommandHandlerNotFoundException}
 * if no command handler is registered for a given command.
 * 
 * @author Joel Jeremy Marquez
 */
public class RequiredCommandHandlerProvider implements CommandHandlerProvider {

    private final CommandHandlerProvider decoratedCommandHandlerProvider;

    /**
     * Constructor.
     * @param decoratedCommandHandlerProvider Command handler provider to get command handlers from.
     */
    public RequiredCommandHandlerProvider(CommandHandlerProvider decoratedCommandHandlerProvider) {
        this.decoratedCommandHandlerProvider = 
            requireNonNull(decoratedCommandHandlerProvider, "decoratedCommandHandlerProvider");
    }

    /**
	 * Get command handler for the given command type. If there is no registered command handler,
     * this will throw a {@link CommandHandlerNotFoundException}.
	 * @param <TCommand> The command type.
	 * @param commandType The command type.
	 * @return The command handler instance that is registered for the command type.
	 */
    @Override
    public <TCommand> Optional<CommandHandler<TCommand>> getCommandHandlerFor(
            Class<TCommand> commandType) {

        requireNonNull(commandType, "commandType");

        Optional<CommandHandler<TCommand>> result = 
            decoratedCommandHandlerProvider.getCommandHandlerFor(commandType);
        
        if (!result.isPresent()) {
            throw new CommandHandlerNotFoundException(commandType);
        }

        return result;
    }
    
}