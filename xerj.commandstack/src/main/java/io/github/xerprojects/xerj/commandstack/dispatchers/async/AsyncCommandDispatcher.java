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

package io.github.xerprojects.xerj.commandstack.dispatchers.async;

import static io.github.xerprojects.xerj.commandstack.internal.utils.Arguments.requireNonNull;

import java.util.concurrent.ExecutorService;

import io.github.xerprojects.xerj.commandstack.CommandDispatcher;

/**
 * Command dispatcher decorator that enables decorated command dispatcher to 
 * execute commands asynchronously. Commands that are dispatched via this dispatcher 
 * will be executed asynchronously via the provided {@link ExecutorService}.
 * 
 * Commands that are marked with marker interface {@link SynchronousCommand} 
 * will be executed synchrously (not via {@link ExecutorService}).
 * 
 * @author Joel Jeremy Marquez
 */
public class AsyncCommandDispatcher implements CommandDispatcher {

    private final CommandDispatcher decoratedDispatcher;
    private final ExecutorService executorService;

    /**
     * Constructor.
     * @param decorateDispatcher Decorated command dispatcher.
     * @param executorService Executor service that will be used to execute commands.
     */
    public AsyncCommandDispatcher(
            CommandDispatcher decorateDispatcher, 
            ExecutorService executorService) {
        this.decoratedDispatcher = 
            requireNonNull(decorateDispatcher, "decoratedDispatcher");
        this.executorService = requireNonNull(executorService, "executorService");;
    }

    /**
     * Asynchronously dispatch command to its registered command handler.
     * If the command implements the marker interface {@link SynchronousCommand},
     * then the command handler will be executed synchronously.
     */
    @Override
    public <TCommand> void send(TCommand command) {

        requireNonNull(command, "command");

        if (command instanceof SynchronousCommand) {
            decoratedDispatcher.send(command);
        } else {
            executorService.execute(() -> {
                decoratedDispatcher.send(command);
            });
        }
    }
    
}
