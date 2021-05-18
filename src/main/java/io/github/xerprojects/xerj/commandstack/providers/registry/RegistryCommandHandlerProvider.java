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

package io.github.xerprojects.xerj.commandstack.providers.registry;

import static io.github.xerprojects.xerj.commandstack.utils.Arguments.requireNonNull;

import java.util.Optional;
import java.util.function.Consumer;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.CommandHandlerProvider;
import io.github.xerprojects.xerj.commandstack.providers.registry.internal.ConcurrentHashMapRegistry;

/**
 * Simple command handler provider that resolves command handlers via an internal map/registry.
 * 
 * @author Joel Jeremy Marquez
 */
public class RegistryCommandHandlerProvider implements CommandHandlerProvider {

	private final ConcurrentHashMapRegistry registry = new ConcurrentHashMapRegistry();

	/**
	 * Constructor.
	 * @param registryConfiguration Command handler registry configuration.
	 */
	public RegistryCommandHandlerProvider(Consumer<CommandHandlerRegistry> registryConfiguration) {
		requireNonNull(registryConfiguration, "registryConfiguration");
		registryConfiguration.accept(registry);
	}

	/**
	 * Get command handler for the given command type.
	 * @param <TCommand> The command type.
	 * @param commandType The command type.
	 * @return The command handler instance that is registered for the command type.
	 * If there is no command handler was registered, an empty Optional will be returned.
	 */
	@Override
	public <TCommand> Optional<CommandHandler<TCommand>> getCommandHandlerFor(
			Class<TCommand> commandType) {

		requireNonNull(commandType, "commandType");

		return registry.getCommandHandlerFor(commandType);
	}
}
