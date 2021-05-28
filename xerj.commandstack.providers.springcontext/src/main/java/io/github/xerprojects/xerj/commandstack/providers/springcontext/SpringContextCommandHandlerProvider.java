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

package io.github.xerprojects.xerj.commandstack.providers.springcontext;

import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.CommandHandlerProvider;
import io.github.xerprojects.xerj.commandstack.exceptions.DuplicateCommandHandlerFoundException;

/**
 * Command handler provider implementation that gets its command handler instances
 * from Spring's {@link ApplicationContext}.
 * 
 * @author Joel Jeremy Marquez
 */
public class SpringContextCommandHandlerProvider implements CommandHandlerProvider {

    private final ApplicationContext appContext;

    /**
     * Constructor.
     * @param appContext Spring's application context.
     */
    public SpringContextCommandHandlerProvider(ApplicationContext appContext) {

        if (appContext == null) {
            throw new IllegalArgumentException("Spring application context must not be null.");
        }

        this.appContext = appContext;
    }

    /**
	 * Get command handler for the given command type. If multiple command handlers are found for
     * the given command type, a {@link SpringDuplicateCommandHandlerFoundException} will be thrown.
	 * @param <TCommand> The command type.
	 * @param commandType The command type.
	 * @return The command handler instance that is registered for the command type.
	 * If there is no command handler was registered, an empty Optional will be returned.
	 */
    @Override
	public <TCommand> Optional<CommandHandler<TCommand>> getCommandHandlerFor(
        Class<TCommand> commandType) {

        if (commandType == null) {
            throw new IllegalArgumentException("Command type must not be null.");
        }

        String[] handlerBeanNames = appContext.getBeanNamesForType(
            ResolvableType.forClassWithGenerics(CommandHandler.class, commandType));

        if (handlerBeanNames.length == 0) {
            return Optional.empty();
        }

        if (handlerBeanNames.length > 1) {
            throw new DuplicateCommandHandlerFoundException(commandType, 
                "Multiple command handlers that handle " + commandType + 
                " have been detected by Spring Application Context.");
        }

        @SuppressWarnings("unchecked")
        CommandHandler<TCommand> instance = appContext.getBean(handlerBeanNames[0], CommandHandler.class);
        return Optional.ofNullable(instance);
    }
}