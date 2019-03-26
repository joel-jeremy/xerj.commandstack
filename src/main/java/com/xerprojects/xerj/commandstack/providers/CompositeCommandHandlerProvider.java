package com.xerprojects.xerj.commandstack.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.xerprojects.xerj.commandstack.Command;
import com.xerprojects.xerj.commandstack.CommandHandler;
import com.xerprojects.xerj.commandstack.CommandHandlerProvider;
import com.xerprojects.xerj.commandstack.exceptions.CommandHandlerNotFoundException;
import com.xerprojects.xerj.commandstack.exceptions.DuplicateCommandHandlerFoundException;

public class CompositeCommandHandlerProvider implements CommandHandlerProvider {

	private final List<CommandHandlerProvider> providers = new ArrayList<>();

	public CompositeCommandHandlerProvider(Iterable<CommandHandlerProvider> providers) {
		
		if (providers == null) {
			throw new IllegalArgumentException("Providers must not be null.");
		}
		
		providers.forEach(this.providers::add);

		if (this.providers.size() == 0) {
			throw new IllegalArgumentException("Providers must not be empty.");
		}
	}
	
	@Override
	public <TCommand extends Command> Optional<CommandHandler<TCommand>> getCommandHandlerFor(
			Class<TCommand> commandType) {
		
		List<Optional<CommandHandler<TCommand>>> resolvedCommandHandlers = providers.stream()
			.map(r -> r.getCommandHandlerFor(commandType))
			.filter(r -> r.isPresent())
			.collect(Collectors.toList());
		
		// All resolvers returned an empty Optional object.
		if (resolvedCommandHandlers.size() == 0) {
			throw new CommandHandlerNotFoundException(commandType);
		}
		
		// Multiple resolvers have a registered command handler for the command.
		if (resolvedCommandHandlers.size() > 1) {
			throw new DuplicateCommandHandlerFoundException(commandType);
		}
		
		return resolvedCommandHandlers.get(0);
	}
}
