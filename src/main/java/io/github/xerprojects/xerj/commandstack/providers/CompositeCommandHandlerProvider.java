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

import static io.github.xerprojects.xerj.commandstack.utils.Arguments.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.CommandHandlerProvider;
import io.github.xerprojects.xerj.commandstack.exceptions.DuplicateCommandHandlerFoundException;

/**
 * Command handler provider that aggregates command handlers 
 * from one or more command handler providers.
 * 
 * @author Joel Jeremy Marquez
 */
public class CompositeCommandHandlerProvider implements CommandHandlerProvider {

	private final ArrayList<CommandHandlerProvider> providers = new ArrayList<>();

	/**
	 * Constructor.
	 * @param providers Command handler providers to get command handlers from.
	 */
	public CompositeCommandHandlerProvider(Iterable<CommandHandlerProvider> providers) {
		
		requireNonNull(providers, "providers");

		if (!providers.iterator().hasNext()){
			throw new IllegalArgumentException("Providers list must not be empty.");
		}
		
		providers.forEach(this.providers::add);
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
		
		List<Optional<CommandHandler<TCommand>>> resolvedCommandHandlers = providers.stream()
			.map(provider -> provider.getCommandHandlerFor(commandType))
			.filter(handler -> handler.isPresent())
			.collect(Collectors.toList());
		
		// Multiple resolvers have a registered command handler for the command.
		if (resolvedCommandHandlers.size() > 1) {
			throw new DuplicateCommandHandlerFoundException(commandType);
		}
		
		return resolvedCommandHandlers.get(0);
	}
}
