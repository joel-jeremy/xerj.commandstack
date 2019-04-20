package io.github.xerprojects.xerj.commandstack.providers;

import java.util.Optional;

import io.github.xerprojects.xerj.commandstack.Command;
import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.CommandHandlerProvider;
import io.github.xerprojects.xerj.commandstack.exceptions.CommandHandlerNotFoundException;

public class RequiredCommandHandlerProvider implements CommandHandlerProvider {

    private final CommandHandlerProvider decorated;

    public RequiredCommandHandlerProvider(CommandHandlerProvider decorated) {
        if (decorated == null) {
            throw new IllegalArgumentException("Decorated command handler provider must not be null.");
        }

        this.decorated = decorated;
    }

    @Override
    public <TCommand extends Command> Optional<CommandHandler<TCommand>> getCommandHandlerFor(
            Class<TCommand> commandType) {
        Optional<CommandHandler<TCommand>> result = decorated.getCommandHandlerFor(commandType);
        
        if (!result.isPresent()) {
            throw new CommandHandlerNotFoundException(commandType);
        }

        return result;
    }
    
}