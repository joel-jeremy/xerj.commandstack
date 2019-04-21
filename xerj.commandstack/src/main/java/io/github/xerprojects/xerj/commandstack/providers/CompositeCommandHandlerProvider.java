package io.github.xerprojects.xerj.commandstack.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.github.xerprojects.xerj.commandstack.Command;
import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.CommandHandlerProvider;
import io.github.xerprojects.xerj.commandstack.exceptions.CommandHandlerNotFoundException;
import io.github.xerprojects.xerj.commandstack.exceptions.DuplicateCommandHandlerFoundException;

public class CompositeCommandHandlerProvider implements CommandHandlerProvider {

	private final ArrayList<CommandHandlerProvider> providers = new ArrayList<>();

	public CompositeCommandHandlerProvider(Iterable<CommandHandlerProvider> providers) {
		
		if (providers == null) {
			throw new IllegalArgumentException("Providers must not be null.");
		}

		if (!providers.iterator().hasNext()){
			throw new IllegalArgumentException("Providers list must not be empty.");
		}
		
		providers.forEach(this.providers::add);
	}
	
	@Override
	public <TCommand extends Command> Optional<CommandHandler<TCommand>> getCommandHandlerFor(
			Class<TCommand> commandType) {

		if (commandType == null) {
			throw new IllegalArgumentException("Command type must not be null.");
		}
		
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
