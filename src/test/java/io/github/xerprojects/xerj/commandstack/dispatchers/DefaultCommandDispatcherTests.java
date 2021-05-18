package io.github.xerprojects.xerj.commandstack.dispatchers;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.CommandHandlerProvider;
import io.github.xerprojects.xerj.commandstack.TestCommand;
import io.github.xerprojects.xerj.commandstack.exceptions.CommandStackException;

@ExtendWith(MockitoExtension.class)
public class DefaultCommandDispatcherTests {

	@Nested
	public class Constructor {
		@Test
		@DisplayName("should throw when commandHandlerProvider argument is null")
		public void test1() {
			assertThrows(IllegalArgumentException.class, () -> {
				new DefaultCommandDispatcher(null);
			});
		}

		@Test
		@DisplayName(
			"should throw when commandHandlerProvider argument is null " +
			"and commandHandlerNotFoundHandler argument is not null")
		public void test2(@Mock Consumer<Class<?>> commandHandlerNotFoundHandler) {
			assertThrows(IllegalArgumentException.class, () -> {
				new DefaultCommandDispatcher(null, commandHandlerNotFoundHandler);
			});
		}

		@Test
		@DisplayName(
			"should throw when commandHandlerNotFoundHandler argument is null")
		public void test3(@Mock CommandHandlerProvider commandHandlerProvider) {
			assertThrows(IllegalArgumentException.class, () -> {
				new DefaultCommandDispatcher(commandHandlerProvider, null);
			});
		}
	}
	
	@Nested
	public class SendMethod {
		@Test
		@DisplayName("should send command to command handler")
		public void test1(
				@Mock CommandHandlerProvider mockProvider,
				@Mock CommandHandler<TestCommand> mockHandler) {

			when(mockProvider.getCommandHandlerFor(TestCommand.class))
				.thenReturn(Optional.of(mockHandler));
			
			var commandDispatcher = new DefaultCommandDispatcher(mockProvider);
			
			var testCommand = new TestCommand();
			
			commandDispatcher.send(testCommand);
			
			verify(mockHandler).handle(testCommand);
		}
		
		@Test
		@DisplayName("should send all commands to command handler")
		public void test2(
				@Mock CommandHandlerProvider mockProvider,
				@Mock CommandHandler<TestCommand> mockHandler) {

			when(mockProvider.getCommandHandlerFor(TestCommand.class))
				.thenReturn(Optional.of(mockHandler));
				
			var commandDispatcher = new DefaultCommandDispatcher(mockProvider);
			
			var command1 = new TestCommand();
			var command2 = new TestCommand();
			var command3 = new TestCommand();
			
			commandDispatcher.send(command1);
			commandDispatcher.send(command2);
			commandDispatcher.send(command3);
			
			verify(mockHandler).handle(command1);
			verify(mockHandler).handle(command2);
			verify(mockHandler).handle(command3);
		}
		
		@Test
		@DisplayName("should throw when command argument is null")
		public void test3(@Mock CommandHandlerProvider mockProvider) {	
			assertThrows(IllegalArgumentException.class, () -> {
				var commandDispatcher = new DefaultCommandDispatcher(mockProvider);
				// Null command.
				commandDispatcher.send(null);
			});
		}

		@Test
		@DisplayName("should propagate exception from command handler")
		public void test5(
				@Mock CommandHandlerProvider mockProvider,
				@Mock CommandHandler<TestCommand> mockHandler) {

			doThrow(RuntimeException.class).when(mockHandler)
				.handle(any(TestCommand.class));

			when(mockProvider.getCommandHandlerFor(TestCommand.class))
				.thenReturn(Optional.of(mockHandler));
	
			assertThrows(RuntimeException.class, () -> {

				var testCommand = new TestCommand();
				
				var commandDispatcher = new DefaultCommandDispatcher(mockProvider);
				
				commandDispatcher.send(testCommand);
			});
		}

		@Test
		@DisplayName("should throw when no command handler provider returns a null Optional")
		public void test6(@Mock CommandHandlerProvider mockProvider) {

			// Returns null instead of Optional.empty().
			when(mockProvider.getCommandHandlerFor(any()))
				.thenReturn(null);
			
			assertThrows(CommandStackException.class, () -> {
				var commandDispatcher = new DefaultCommandDispatcher(mockProvider);
				commandDispatcher.send(new TestCommand());
			});
		}

		@Test
		@DisplayName("should invoke command handler not found handler is no command handler is found")
		public void test7(@Mock CommandHandlerProvider mockProvider,
				@Mock Consumer<Class<?>> mockCommandHandlerNotFoundHandler) {

			var testCommand = new TestCommand();

			when(mockProvider.getCommandHandlerFor(any()))
				.thenReturn(Optional.empty());
			
			var commandDispatcher = new DefaultCommandDispatcher(
				mockProvider, mockCommandHandlerNotFoundHandler);
			
			commandDispatcher.send(testCommand);

			// should be the correct command type.
			verify(mockCommandHandlerNotFoundHandler).accept(testCommand.getClass());
		}
	}
}
