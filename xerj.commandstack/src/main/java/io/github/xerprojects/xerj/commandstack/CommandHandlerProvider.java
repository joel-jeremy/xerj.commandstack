package io.github.xerprojects.xerj.commandstack;

import java.util.Optional;

public interface CommandHandlerProvider {
	<TCommand extends Command> Optional<CommandHandler<TCommand>> getCommandHandlerFor(Class<TCommand> commandType);
}
