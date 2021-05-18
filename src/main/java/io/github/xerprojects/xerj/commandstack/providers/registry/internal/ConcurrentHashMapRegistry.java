package io.github.xerprojects.xerj.commandstack.providers.registry.internal;

import static io.github.xerprojects.xerj.commandstack.utils.Arguments.requireNonNull;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.CommandHandlerProvider;
import io.github.xerprojects.xerj.commandstack.exceptions.DuplicateCommandHandlerRegistrationException;
import io.github.xerprojects.xerj.commandstack.providers.registry.CommandHandlerRegistry;

/**
 * Default internal implementation of {@link CommandHandlerRegistry}
 * which stores registrations via a {@link ConcurrentHashMap}.
 * 
 * @author Joel Jeremy Marquez
 */
public class ConcurrentHashMapRegistry implements CommandHandlerRegistry, CommandHandlerProvider {

    private final ConcurrentHashMap<Class<?>, RegisteredCommandHandler<?>>
        registeredHandlers = new ConcurrentHashMap<>();

    /**
     * Register command handler. This will throw a {@link DuplicateCommandHandlerRegistrationException}
     * when a command is registered more than once.
     * @param <TCommand> The command type.
     * @param commandType The command type.
     * @param instanceFactory Factory which returns an instance of 
     * a command handler that can handle commands of the given command type.
     * @return This command handler registry instance.
     */
    @Override
    public <TCommand> CommandHandlerRegistry registerCommandHandler(
            Class<TCommand> commandType,
            Supplier<CommandHandler<TCommand>> instanceFactory) {

        requireNonNull(commandType, "commandType");
        requireNonNull(instanceFactory, "instanceFactory");

        if (registeredHandlers.containsKey(commandType)) {
            throw new DuplicateCommandHandlerRegistrationException(commandType);
        }

        registeredHandlers.put(commandType, 
            new RegisteredCommandHandler<>(commandType, instanceFactory));

        return this;
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
        
        @SuppressWarnings("unchecked")
        RegisteredCommandHandler<TCommand> registeredHandler =
            (RegisteredCommandHandler<TCommand>)registeredHandlers.get(commandType);

        if (registeredHandler == null) {
            return Optional.empty();
        }

        return Optional.of(registeredHandler.getInstance());
    }
}