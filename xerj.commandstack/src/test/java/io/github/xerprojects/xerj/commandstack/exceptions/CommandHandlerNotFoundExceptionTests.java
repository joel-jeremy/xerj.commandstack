package io.github.xerprojects.xerj.commandstack.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.github.xerprojects.xerj.commandstack.testentities.TestCommand;

public class CommandHandlerNotFoundExceptionTests {
    @Nested
    public class Constructor {
        @Test
        @DisplayName("should set command type correctly")
        public void test1() {
            CommandHandlerNotFoundException ex = 
                new CommandHandlerNotFoundException(TestCommand.class);

            assertEquals(TestCommand.class, ex.getCommandType());
        }

        @Test
        @DisplayName("should set exception message correctly")
        public void test2() {
            String customExceptionMessage = "My custom exception message.";

            CommandHandlerNotFoundException ex = 
                new CommandHandlerNotFoundException(
                    TestCommand.class, customExceptionMessage);

            assertEquals(customExceptionMessage, ex.getMessage());
        }

        @Test
        @DisplayName("should set exception cause correctly")
        public void test3() {
            RuntimeException cause = new RuntimeException("Exception cause.");

            CommandHandlerNotFoundException ex = 
                new CommandHandlerNotFoundException(
                    TestCommand.class, cause);

            assertEquals(cause, ex.getCause());
        }

        @Test
        @DisplayName("should set exception message and cause correctly")
        public void test4() {
            String customExceptionMessage = "My custom exception message.";
            RuntimeException cause = new RuntimeException("Exception cause.");

            CommandHandlerNotFoundException ex = 
                new CommandHandlerNotFoundException(
                    TestCommand.class, customExceptionMessage, cause);

            assertEquals(customExceptionMessage, ex.getMessage());
            assertEquals(cause, ex.getCause());
        }

        @Test
        @DisplayName("should set commmand type, exception message and cause correctly")
        public void test5() {
            Class<TestCommand> commandType = TestCommand.class;
            String customExceptionMessage = "My custom exception message.";
            RuntimeException cause = new RuntimeException("Exception cause.");

            CommandHandlerNotFoundException ex = 
                new CommandHandlerNotFoundException(
                    commandType, customExceptionMessage, cause);
            
            assertEquals(commandType, ex.getCommandType());
            assertEquals(customExceptionMessage, ex.getMessage());
            assertEquals(cause, ex.getCause());
        }

        @Test
        @DisplayName("should set default exception message if null is provided")
        public void test6() {
            String customExceptionMessage = null;

            CommandHandlerNotFoundException ex = 
                new CommandHandlerNotFoundException(
                    TestCommand.class, customExceptionMessage);

            assertNotNull(ex.getMessage());
            assertFalse(ex.getMessage().isEmpty());
        }

        @Test
        @DisplayName("should set default exception message if empty is provided")
        public void test7() {
            String customExceptionMessage = "";

            CommandHandlerNotFoundException ex = 
                new CommandHandlerNotFoundException(
                    TestCommand.class, customExceptionMessage);

            assertNotNull(ex.getMessage());
            assertFalse(ex.getMessage().isEmpty());
        }
    }

    @Nested
    public class GetCommandTypeMethod {
        @Test
        @DisplayName("should return correct command type")
        public void test1() {
            Class<TestCommand> commandType = TestCommand.class;

            CommandHandlerNotFoundException ex = 
                new CommandHandlerNotFoundException(commandType);

            assertEquals(commandType, ex.getCommandType());
        }
    }
}
