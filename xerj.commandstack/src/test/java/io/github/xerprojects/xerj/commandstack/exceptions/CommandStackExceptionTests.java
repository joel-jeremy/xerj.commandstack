package io.github.xerprojects.xerj.commandstack.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class CommandStackExceptionTests {
    @Nested
    public class Constructor {

        @Test
        @DisplayName("should set exception message correctly")
        public void test1() {
            String customExceptionMessage = "My custom exception message.";

            CommandStackException ex = 
                new CommandStackException(customExceptionMessage);

            assertEquals(customExceptionMessage, ex.getMessage());
        }

        @Test
        @DisplayName("should set exception cause correctly")
        public void test2() {
            String customExceptionMessage = "My custom exception message.";
            RuntimeException cause = new RuntimeException("Exception cause.");

            CommandStackException ex = new CommandStackException(
                customExceptionMessage, cause);

            assertEquals(cause, ex.getCause());
        }

        @Test
        @DisplayName("should set exception message and cause correctly")
        public void test3() {
            String customExceptionMessage = "My custom exception message.";
            RuntimeException cause = new RuntimeException("Exception cause.");

            CommandStackException ex = new CommandStackException(
                customExceptionMessage, cause);

            assertEquals(customExceptionMessage, ex.getMessage());
            assertEquals(cause, ex.getCause());
        }
    }
}
