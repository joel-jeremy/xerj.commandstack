package io.github.xerprojects.xerj.commandstack.providers.guice;

import java.util.Optional;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.util.Types;

import io.github.xerprojects.xerj.commandstack.Command;
import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.CommandHandlerProvider;

public class GuiceCommandHandlerProvider implements CommandHandlerProvider {

    private final Injector injector;

    public GuiceCommandHandlerProvider(Injector injector) {

        if (injector == null) {
            throw new IllegalArgumentException("Guice injector must not be null.");
        }
        
        this.injector = injector;
    }

    @Override
	public <TCommand extends Command> Optional<CommandHandler<TCommand>> getCommandHandlerFor(
        Class<TCommand> commandType) {

        if (commandType == null) {
            throw new IllegalArgumentException("Command type must not be null.");
        }
        
        @SuppressWarnings("unchecked")
        CommandHandler<TCommand> instance = (CommandHandler<TCommand>) injector.getInstance(
            Key.get(Types.newParameterizedType(CommandHandler.class, commandType)));

        return Optional.ofNullable(instance);
    }
}