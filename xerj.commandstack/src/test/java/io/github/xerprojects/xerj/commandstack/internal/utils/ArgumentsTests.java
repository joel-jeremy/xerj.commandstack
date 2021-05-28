package io.github.xerprojects.xerj.commandstack.internal.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.github.xerprojects.xerj.commandstack.testentities.TestCommand;

public class ArgumentsTests {

    @Nested
    public class RequireMethod {
        @Test
        @DisplayName("should throw when condition is matched")
        public void test1() {

            TestCommand testCommand = null;

            assertThrows(IllegalArgumentException.class, () -> {
                Arguments.require(testCommand, c -> c == null, "Command is null.");
            });
        }

        @Test
        @DisplayName("should throw with the specified exception message")
        public void test2() {

            String exceptionMessage = "Command is null.";
            TestCommand testCommand = null;

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
                Arguments.require(testCommand, c -> c == null, exceptionMessage);
            });

            assertEquals(exceptionMessage, ex.getMessage());
        }
        
        @Test
        @DisplayName("should return non-null object")
        public void test3() {
            String exceptionMessage = "Command is null.";
            TestCommand testCommand = new TestCommand();
            TestCommand nonNullCommand = Arguments.require(testCommand, c -> c == null, exceptionMessage);
            assertNotNull(nonNullCommand);
            assertEquals(testCommand, nonNullCommand);
        }

        @Test
        @DisplayName("should throw if condition is null")
        public void test4() {
            TestCommand testCommand = new TestCommand();

            assertThrows(IllegalArgumentException.class, () -> {
                Arguments.require(testCommand, null, "My exception message");
            });
        }
    }

    @Nested
    public class RequireNonNullMethod {
        @Test
        @DisplayName("should throw when object argument is null")
        public void test1() {
            TestCommand testCommand = null;

            assertThrows(IllegalArgumentException.class, () -> {
                Arguments.requireNonNull(testCommand, "testCommand");
            });
        }
        
        @Test
        @DisplayName("should return non-null object")
        public void test3() {
            TestCommand testCommand = new TestCommand();
            TestCommand nonNullCommand = Arguments.requireNonNull(testCommand, "testCommand");
            assertNotNull(nonNullCommand);
            assertEquals(testCommand, nonNullCommand);
        }
    }

    @Nested
    public class ArgumentsStringsTests {
        @Nested
        public class RequireNonNullOrEmptyMethod {
            @Test
            @DisplayName("should throw when string argument is null")
            public void test1() {
                String testString = null;

                assertThrows(IllegalArgumentException.class, () -> {
                    Arguments.Strings.requireNonNullOrEmpty(testString, "testString");
                });
            }

            @Test
            @DisplayName("should throw when string argument is empty")
            public void test2() {
                String testString = "";

                assertThrows(IllegalArgumentException.class, () -> {
                    Arguments.Strings.requireNonNullOrEmpty(testString, "testString");
                });
            }

            @Test
            @DisplayName("should return non-null string")
            public void test3() {
                String testString = "myTestString";
                String nonNullString = Arguments.Strings.requireNonNullOrEmpty(testString, "testString");
                assertNotNull(nonNullString);
            }
        }
    }

}