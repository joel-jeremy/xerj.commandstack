package io.github.xerprojects.xerj.commandstack.providers.registry.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.exceptions.CommandStackException;
import io.github.xerprojects.xerj.commandstack.testentities.TestCommand;

@ExtendWith(MockitoExtension.class)
public class RegisteredCommandHandlerTests {
    @Nested
    public class Constructor {
        @Test
        @DisplayName("should throw if commandType argument is null")
        @SuppressWarnings("exports")
        public void test1(
                @Mock Supplier<CommandHandler<TestCommand>> mockInstanceFactory) {
            assertThrows(IllegalArgumentException.class, () -> {
                new RegisteredCommandHandler<>(null, mockInstanceFactory);
            });
        }

        @Test
        @DisplayName("should throw if commandHandlerInstanceFactory argument is null")
        public void test2() {
            assertThrows(IllegalArgumentException.class, () -> {
                new RegisteredCommandHandler<>(TestCommand.class, null);
            });
        }
    }

    @Nested
    public class GetCommandTypeMethod {
        @Test
        @DisplayName("should return correct comand type")
        @SuppressWarnings("exports")
        public void test1(
                @Mock Supplier<CommandHandler<TestCommand>> mockInstanceFactory) {
                    
            var registerCommandHandler = new RegisteredCommandHandler<TestCommand>(
                TestCommand.class, mockInstanceFactory);

            assertEquals(TestCommand.class, registerCommandHandler.getCommandType());
        }
    }

    @Nested
    public class GetInstanceMethod {
        @Test
        @DisplayName("should return command handler instance")
        @SuppressWarnings("exports")
        public void test1(
                @Mock Supplier<CommandHandler<TestCommand>> mockInstanceFactory,
                @Mock(stubOnly = true) CommandHandler<TestCommand> stubHandler) {

            when(mockInstanceFactory.get()).thenReturn(stubHandler);

            var registerCommandHandler = new RegisteredCommandHandler<TestCommand>(
                TestCommand.class, mockInstanceFactory);

            CommandHandler<TestCommand> resolvedCommandHandler = registerCommandHandler.getInstance();

            // verify instance came from factory.
            verify(mockInstanceFactory).get();

            assertEquals(stubHandler, resolvedCommandHandler);
        }

        @Test
        @DisplayName("should throw if command handler instance factory returns null")
        @SuppressWarnings("exports")
        public void test2(
                @Mock Supplier<CommandHandler<TestCommand>> mockInstanceFactory) {

            when(mockInstanceFactory.get()).thenReturn(null);

            var registerCommandHandler = new RegisteredCommandHandler<TestCommand>(
                TestCommand.class, mockInstanceFactory);

            assertThrows(CommandStackException.class, () -> {
                registerCommandHandler.getInstance();
            });
        }

        @Test
        @DisplayName("should wrap exception if command handler instance factory throws an exception")
        @SuppressWarnings("exports")
        public void test3(
                @Mock Supplier<CommandHandler<TestCommand>> mockInstanceFactory,
                @Mock(stubOnly = true) RuntimeException mockException) {

            when(mockInstanceFactory.get()).thenThrow(mockException);

            var registeredCommandHandler = new RegisteredCommandHandler<TestCommand>(
                TestCommand.class, mockInstanceFactory);
                
            CommandStackException exception = 
                assertThrows(CommandStackException.class, () -> {
                    registeredCommandHandler.getInstance();
                });

            // CommandStackException should wrap the original exception.
            assertEquals(mockException, exception.getCause());
        }
    }
}
