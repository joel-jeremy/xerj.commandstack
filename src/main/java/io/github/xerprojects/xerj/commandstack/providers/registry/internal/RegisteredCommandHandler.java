package io.github.xerprojects.xerj.commandstack.providers.registry.internal;

import static io.github.xerprojects.xerj.commandstack.internal.utils.Arguments.requireNonNull;

import java.util.function.Supplier;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.exceptions.CommandStackException;

/**
 * Stores the command handler instance factory that is mapped to a command type.
 * @param <TCommand> The command type associated with this registration.
 * 
 * @author Joel Jeremy Marquez
 */
public class RegisteredCommandHandler<TCommand> {
		
    private final Class<TCommand> commandType;
    private final Supplier<CommandHandler<TCommand>> commandHandlerInstanceFactory;
     
    /**
     * Constructor.
     * @param commandType The command type.
     * @param commandHandlerInstanceFactory Command handler instance factory.
     */
    public RegisteredCommandHandler(
            Class<TCommand> commandType, 
            Supplier<CommandHandler<TCommand>> commandHandlerInstanceFactory) {
        this.commandType = requireNonNull(commandType, "commandType");
        this.commandHandlerInstanceFactory = 
            requireNonNull(commandHandlerInstanceFactory, "commandHandlerInstanceFactory");
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
     * This will validate the command handler instance returned by the command handler instance factory
     * and will throw a {@link CommandStackException} if:
     * <ul>
     *   <li>Command handler instance factory has thrown an exception, or</li>
     *   <li>Command handler instance factory returned null.</li>
     * </ul>
     * @return The command handler instance that can handle the registered comand type.
     */
    public CommandHandler<TCommand> getInstance() {
        CommandHandler<TCommand> instance;
        try {
            instance = commandHandlerInstanceFactory.get();
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