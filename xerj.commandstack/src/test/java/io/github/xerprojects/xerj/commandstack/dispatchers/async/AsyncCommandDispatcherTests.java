package io.github.xerprojects.xerj.commandstack.dispatchers.async;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.xerprojects.xerj.commandstack.CommandDispatcher;
import io.github.xerprojects.xerj.commandstack.testentities.TestCommand;
import io.github.xerprojects.xerj.commandstack.testentities.TestSynchronousCommand;

@ExtendWith(MockitoExtension.class)
public class AsyncCommandDispatcherTests {

    @Nested
	public class Constructor {
		@Test
		@DisplayName("should throw when executor service is null")
		public void test1(@Mock(stubOnly = true) ExecutorService stubExecutorService) {
			assertThrows(IllegalArgumentException.class, () -> {
				new AsyncCommandDispatcher(null, stubExecutorService);
			});
		}

        @Test
		@DisplayName("should throw when decorated provider is null")
		public void test2(@Mock(stubOnly = true) CommandDispatcher stubCommandDispatcher) {
			assertThrows(IllegalArgumentException.class, () -> {
				new AsyncCommandDispatcher(stubCommandDispatcher, null);
			});
		}
	}

    @Nested
    public class SendMethod {
        @Test
        @DisplayName("should dispatch command via executor service")
        public void test1(
                @Mock CommandDispatcher mockDecoratedCommandDispatcher,
                @Mock ExecutorService mockExecutorService) {

            var testCommand = new TestCommand();

            executeRunnableWhenInvoked(mockExecutorService);

            var commandDispatcher = 
                new AsyncCommandDispatcher(mockDecoratedCommandDispatcher, mockExecutorService);

            commandDispatcher.send(testCommand);

            verify(mockExecutorService).execute(any(Runnable.class));
            verify(mockDecoratedCommandDispatcher).send(testCommand);
        }

        @Test
        @DisplayName("should not dispatch command via executor service for synchronous commands")
        public void test2(
                @Mock CommandDispatcher mockDecoratedCommandDispatcher,
                @Mock ExecutorService mockExecutorService) {

            var testCommand = new TestSynchronousCommand();

            var commandDispatcher = 
                new AsyncCommandDispatcher(mockDecoratedCommandDispatcher, mockExecutorService);

            commandDispatcher.send(testCommand);

            verify(mockExecutorService, never()).execute(any(Runnable.class));
            verify(mockDecoratedCommandDispatcher).send(testCommand);
        }

        @Test
		@DisplayName("should throw when command argument is null")
		public void test3(
                @Mock(stubOnly = true) CommandDispatcher stubCommandDispatcher,
                @Mock(stubOnly = true) ExecutorService stubExecutorService) {

			assertThrows(IllegalArgumentException.class, () -> {
				var commandDispatcher = new AsyncCommandDispatcher(
                    stubCommandDispatcher, stubExecutorService);
				// Null command.
				commandDispatcher.send(null);
			});
		}

        private void executeRunnableWhenInvoked(ExecutorService mockExecutorService) {
            doAnswer(invocation -> {
                Runnable arg = invocation.getArgument(0);
                arg.run();
                return CompletableFuture.completedFuture(null);
            }).when(mockExecutorService).execute(any(Runnable.class));
        }
    }
}
