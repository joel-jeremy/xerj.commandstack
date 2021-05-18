package io.github.xerprojects.xerj.commandstack.providers.registry.internal;

import static io.github.xerprojects.xerj.commandstack.utils.Arguments.requireNonNull;

import java.util.function.Supplier;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.exceptions.CommandStackException;

/**
 * Stores the {@link CommandHandlerInstanceFactory} that is mapped to a command type.
 * @param <TCommand> The command type associated with this registration.
 * 
 * @author Joel Jeremy Marquez
 */
public class RegisteredCommandHandler<TCommand> {
		
    private final Class<TCommand> commandType;
    private final Supplier<CommandHandler<TCommand>> instanceFactory;
     
    /**
     * Constructor.
     * @param commandType The command type.
     * @param instanceFactory Command handler instance factory.
     */
    public RegisteredCommandHandler(
            Class<TCommand> commandType, 
            Supplier<CommandHandler<TCommand>> instanceFactory) {
        this.commandType = requireNonNull(commandType, "commandType");
        this.instanceFactory = requireNonNull(instanceFactory, "instanceFactory");
    }

    /**
     * Get the registered command type.
     * @return The command type.
     */
    public Class<TCommand> getCommandType() {
        return commandType;
    }
    
    /**
     * Get a command handler instance.
     * 
     * This will validate the command handler instance returned by the {@link CommandHandlerInstanceFactory}
     * and will throw a {@link CommandHandlerInstanceFactoryException} if:
     * <ul>
     *   <li>{@link CommandHandlerInstanceFactory} has thrown an exception, or</li>
     *   <li>{@link CommandHandlerInstanceFactory} returned null.</li>
     * </ul>
     * @return The command handler instance that can handle the registered comand type.
     */
    public CommandHandler<TCommand> getInstance() {
        CommandHandler<TCommand> instance;
        try {
            instance = instanceFactory.get();
        } catch (Exception e) {
            throw new CommandStackException(
                "Registered command handler instance factory for " + getCommandType() +
                " has thrown an exception. Please check configuration.", e);
        }
            
        if (instance == null) {
            throw new CommandStackException(
                "Registered command handler instance factory for " + getCommandType() +
                " supplied a null instance. Please check configuration.");
        }

        return instance;
    }
}