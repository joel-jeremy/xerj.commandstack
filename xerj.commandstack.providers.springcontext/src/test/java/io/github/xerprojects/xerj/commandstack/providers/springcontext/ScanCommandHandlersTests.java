package io.github.xerprojects.xerj.commandstack.providers.springcontext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.testentities.TestCommandHandler;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.testentities.other.OtherTestCommandHandler;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.testentities.springconfigs.OtherScanConfig;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.testentities.springconfigs.ScanConfig;

public class ScanCommandHandlersTests {
    @Test
    @DisplayName("should detect command handlers from package and subpackages")
    public void test1() {
        try (var appContext = new AnnotationConfigApplicationContext(ScanConfig.class)) {
            // Check all detected command handlers.
            @SuppressWarnings("rawtypes")
            Map<String, CommandHandler> handlers = 
                appContext.getBeansOfType(CommandHandler.class);
            
            assertNotNull(handlers);
            assertTrue(handlers.size() > 0);
            assertTrue(containsCommandHandler(handlers, TestCommandHandler.class));
            assertTrue(containsCommandHandler(handlers, OtherTestCommandHandler.class));
        }
    }

    @Test
    @DisplayName("should detect command handlers on specific packages")
    public void test2() {
        try (var appContext = new AnnotationConfigApplicationContext(OtherScanConfig.class)) {
            // Check all detected command handlers.
            @SuppressWarnings("rawtypes")
            Map<String, CommandHandler> handlers = 
                appContext.getBeansOfType(CommandHandler.class);
            
            assertNotNull(handlers);
            assertTrue(handlers.size() > 0);
            assertTrue(containsCommandHandler(handlers, OtherTestCommandHandler.class));
            // TestCommandHandler is in a higher package so it should not be detected.
            assertFalse(containsCommandHandler(handlers, TestCommandHandler.class));
        }
    }

    private boolean containsCommandHandler(
            @SuppressWarnings("rawtypes")
            Map<String, CommandHandler> handlers,
            Class<?> commandHandlerClass) {
        return handlers.values().stream()
            .anyMatch(h -> h.getClass().equals(commandHandlerClass));
    }
}