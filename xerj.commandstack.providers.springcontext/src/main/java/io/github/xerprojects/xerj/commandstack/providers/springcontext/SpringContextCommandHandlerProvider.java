package io.github.xerprojects.xerj.commandstack.providers.springcontext;

import java.util.Optional;

import org.springframework.context.ApplicationContext;

import io.github.xerprojects.xerj.commandstack.Command;
import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.CommandHandlerProvider;

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

        @SuppressWarnings("unchecked")
        CommandHandler<TCommand> instance = appContext.getBean(CommandHandler.class, commandType);
        return Optional.ofNullable(instance);
    }
}