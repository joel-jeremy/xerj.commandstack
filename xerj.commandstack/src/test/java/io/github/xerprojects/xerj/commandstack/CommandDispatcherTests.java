package io.github.xerprojects.xerj.commandstack;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.github.xerprojects.xerj.commandstack.entities.TestCommand;
import io.github.xerprojects.xerj.commandstack.entities.TestCommandHandler;
import io.github.xerprojects.xerj.commandstack.exceptions.CommandHandlerNotFoundException;
import io.github.xerprojects.xerj.commandstack.providers.RegistryCommandHandlerProvider;

public class CommandDispatcherTests {
	
	@Nested
	public class SendMethod {
		@Test
		public void shouldSendCommandToRegisteredCommandHandler() {
			
			var commandHandler = new TestCommandHandler();
			
			var provider = new RegistryCommandHandlerProvider(config ->
				config.registerCommandHandler(TestCommand.class, () -> commandHandler));
			
			var commandDispatcher = new CommandDispatcher(provider);
			
			var command = new TestCommand();
			
			commandDispatcher.send(command);
			
			assertTrue(commandHandler.hasHandledCommand(command));
		}
		
		@Test
		public void shouldSendCommandsToRegisteredCommandHandler() {
			
			var commandHandler = new TestCommandHandler();
			
			var provider = new RegistryCommandHandlerProvider(config ->
				config.registerCommandHandler(TestCommand.class, () -> commandHandler));
			
			var commandDispatcher = new CommandDispatcher(provider);
			
			var command1 = new TestCommand();
			var command2 = new TestCommand();
			var command3 = new TestCommand();
			
			commandDispatcher.send(command1);
			commandDispatcher.send(command2);
			commandDispatcher.send(command3);
			
			assertTrue(commandHandler.hasHandledCommand(command1));
			assertTrue(commandHandler.hasHandledCommand(command2));
			assertTrue(commandHandler.hasHandledCommand(command3));
		}
		
		@Test
		public void shouldThrowWhenCommandArgumentIsNull() {	
			assertThrows(IllegalArgumentException.class, () -> {
				var provider = new RegistryCommandHandlerProvider(c -> {});
				var commandDispatcher = new CommandDispatcher(provider);
				// Null command.
				commandDispatcher.send(null);
			});
		}
		
		@Test
		public void shouldThrowWhenNoCommandHandlerIsFound() {
			
			assertThrows(CommandHandlerNotFoundException.class, () -> {
				var provider = new RegistryCommandHandlerProvider(config -> {
					// No command handlers.
				});
				
				var commandDispatcher = new CommandDispatcher(provider);
				
				commandDispatcher.send(new TestCommand());
			});
		}

		@Test
		public void shouldPropagateExceptionFromCommandHandler() {
			
			assertThrows(RuntimeException.class, () -> {

				CommandHandler<TestCommand> handler = c -> { throw new RuntimeException("Oops! Exception in handler."); };
				
				var provider = new RegistryCommandHandlerProvider(config -> {
					config.registerCommandHandler(TestCommand.class, () -> handler);
				});
				
				var commandDispatcher = new CommandDispatcher(provider);
				
				commandDispatcher.send(new TestCommand());
			});
		}

		@Test
		public void shouldPropagateExceptionFromCommandHandlerFuture() {
			
			assertThrows(RuntimeException.class, () -> {

				CommandHandler<TestCommand> handler = c -> 
					CompletableFuture.failedFuture(new RuntimeException("Oops! Exception in handler."));
				
				
				var provider = new RegistryCommandHandlerProvider(config -> {
					config.registerCommandHandler(TestCommand.class, () -> handler);
				});
				
				var commandDispatcher = new CommandDispatcher(provider);
				
				commandDispatcher.send(new TestCommand()).join();
			});
		}
	}
}
