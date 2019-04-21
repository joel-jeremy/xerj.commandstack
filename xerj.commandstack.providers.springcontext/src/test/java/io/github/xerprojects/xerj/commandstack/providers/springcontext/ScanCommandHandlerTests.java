package io.github.xerprojects.xerj.commandstack.providers.springcontext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.github.xerprojects.xerj.commandstack.CommandHandler;

import io.github.xerprojects.xerj.commandstack.providers.springcontext.entities.TestCommand;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.entities.TestCommandHandler;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.entities.springconfigs.ScanConfig;

public class ScanCommandHandlerTests {
    @Test
    public void shouldDetectCommandHandlersInPackage() {
        var appContext = new AnnotationConfigApplicationContext(ScanConfig.class);
        @SuppressWarnings("unchecked")
        CommandHandler<TestCommand> handler = appContext.getBean(CommandHandler.class, TestCommand.class);

        assertNotNull(handler);
        assertTrue(handler instanceof TestCommandHandler);
    }
}