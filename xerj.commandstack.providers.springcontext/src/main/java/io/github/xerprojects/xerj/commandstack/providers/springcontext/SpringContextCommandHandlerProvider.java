package io.github.xerprojects.xerj.commandstack.providers.springcontext;

import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;

import io.github.xerprojects.xerj.commandstack.Command;
import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.CommandHandlerProvider;
import io.github.xerprojects.xerj.commandstack.exceptions.DuplicateCommandHandlerFoundException;

public class SpringContextCommandHandlerProvider implements CommandHandlerProvider {

    private final ApplicationContext appContext;

    public SpringContextCommandHandlerProvider(ApplicationContext appContext) {

        if (appContext == null) {
            throw new IllegalArgumentException("Spring application context type must not be null.");
        }

        this.appContext = appContext;
    }

    @Override
	public <TCommand extends Command> Optional<CommandHandler<TCommand>> getCommandHandlerFor(
        Class<TCommand> commandType) {

        if (commandType == null) {
            throw new IllegalArgumentException("Command type must not be null.");
        }

        String[] handlerBeanNames = appContext.getBeanNamesForType(
            ResolvableType.forClassWithGenerics(CommandHandler.class, commandType));

        if (handlerBeanNames.length == 0) {
            return Optional.empty();
        }

        if (handlerBeanNames.length > 1) {
            throw new DuplicateCommandHandlerFoundException(commandType, "Multiple command handlers that handle " + commandType
                + " have been detected by Spring Application Context.");
        }

        @SuppressWarnings("unchecked")
        CommandHandler<TCommand> instance = appContext.getBean(handlerBeanNames[0], CommandHandler.class);
        return Optional.ofNullable(instance);
    }
}