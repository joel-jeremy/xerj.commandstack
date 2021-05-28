package io.github.xerprojects.xerj.commandstack.testentities;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.CommandHandlerProvider;

public class Mocks {

    public static <T> CommandHandler<T> mockCommandHandler() {
        @SuppressWarnings("unchecked")
        var commandHandler = (CommandHandler<T>)mock(CommandHandler.class);
        return commandHandler;
    }

    public static CommandHandlerProvider mockCommandHandlerProvider() {
        return mock(CommandHandlerProvider.class);
    }

    public static <T> CommandHandler<T> stubCommandHandler() {
        @SuppressWarnings("unchecked")
        var commandHandler = (CommandHandler<T>)mock(CommandHandler.class, withSettings().stubOnly());
        return commandHandler;
    }

    public static CommandHandlerProvider stubCommandHandlerProvider() {
        return mock(CommandHandlerProvider.class, withSettings().stubOnly());
    }
}