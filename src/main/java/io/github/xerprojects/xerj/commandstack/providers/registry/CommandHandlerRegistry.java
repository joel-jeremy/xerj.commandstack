package io.github.xerprojects.xerj.commandstack.providers.registry;

import java.util.function.Supplier;

import io.github.xerprojects.xerj.commandstack.CommandHandler;

/**
 * Command handler regsitry.
 * 
 * @author Joel Jeremy Marquez
 */
public interface CommandHandlerRegistry {
    /**
     * 
     * @param <TCommand> The command type.
     * @param commandType The command type.
     * @param instanceFactory Factory which returns an instance of 
     * a command handler that can handle commands of the given command type.
     * @return This command handler registry instance.
     */
    <TCommand> CommandHandlerRegistry registerCommandHandler(
        Class<TCommand> commandType,
        Supplier<CommandHandler<TCommand>> instanceFactory);
}