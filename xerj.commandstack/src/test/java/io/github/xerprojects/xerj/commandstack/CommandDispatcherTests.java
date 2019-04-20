package io.github.xerprojects.xerj.commandstack;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import io.github.xerprojects.xerj.commandstack.entities.BaseCommandHandler;
import io.github.xerprojects.xerj.commandstack.entities.TestCommand;
import io.github.xerprojects.xerj.commandstack.entities.TestCommandHandler;
import io.github.xerprojects.xerj.commandstack.exceptions.CommandHandlerNotFoundException;
import io.github.xerprojects.xerj.commandstack.exceptions.DuplicateCommandHandlerFoundException;
import io.github.xerprojects.xerj.commandstack.providers.CompositeCommandHandlerProvider;
import io.github.xerprojects.xerj.commandstack.providers.RegistryCommandHandlerProvider;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class CommandDispatcherTests {
	
	@Nested
	public class BuilderMethod {
		@Test
		public void shouldNeverReturnNull() {
			CommandDispatcher.Builder builder = CommandDispatcher.builder();
			assertNotNull(builder);
		}
	}
	
	@Nested
	public class BuilderTests {
		
		@Nested
		public class AddCommandHandlerProviderMethod {
			@Test
			public void shouldThrowWhenProviderArgumentIsNull() {
				assertThrows(IllegalArgumentException.class, () -> {
					CommandDispatcher.Builder builder = CommandDispatcher.builder();
					// Add null.
					builder.addCommandHandlerProvider(null);
				});
			}
		}
	}
	
	@Nested
	public class SendMethod {
		@Test
		public void shouldSendCommandToRegisteredCommandHandler() {
			
			var commandHandler = new TestCommandHandler();
			
			CommandHandlerProvider provider = new RegistryCommandHandlerProvider(config ->
				config.registerCommandHandler(TestCommand.class, () -> commandHandler));
			
			CommandDispatcher commandDelegator = CommandDispatcher
					.builder()
					.addCommandHandlerProvider(provider)
					.build();
			
			var command = new TestCommand();
			
			commandDelegator.send(command);
			
			assertTrue(commandHandler.hasHandledCommand(command));
		}
		
		@Test
		public void shouldSendCommandsToRegisteredCommandHandler() {
			
			var commandHandler = new TestCommandHandler();
			
			CommandHandlerProvider provider = new RegistryCommandHandlerProvider(config ->
				config.registerCommandHandler(TestCommand.class, () -> commandHandler));
			
			CommandDispatcher commandDelegator = CommandDispatcher
					.builder()
					.addCommandHandlerProvider(provider)
					.build();
			
			var command1 = new TestCommand();
			var command2 = new TestCommand();
			var command3 = new TestCommand();
			
			commandDelegator.send(command1);
			commandDelegator.send(command2);
			commandDelegator.send(command3);
			
			assertTrue(commandHandler.hasHandledCommand(command1));
			assertTrue(commandHandler.hasHandledCommand(command2));
			assertTrue(commandHandler.hasHandledCommand(command3));
		}
		
		@Test
		public void shouldThrowWhenCommandArgumentIsNull() {	
			assertThrows(IllegalArgumentException.class, () -> {
				CommandDispatcher commandDelegator = CommandDispatcher
						.builder()
						.build();
				
				// Null.
				commandDelegator.send(null);
			});
		}
		
		@Test
		public void shouldThrowWhenNoCommandHandlerIsFound() {
			
			assertThrows(CommandHandlerNotFoundException.class, () -> {
				CommandHandlerProvider provider = new RegistryCommandHandlerProvider(config -> {
					// No command handlers.
				});
				
				CommandDispatcher commandDelegator = CommandDispatcher
						.builder()
						.addCommandHandlerProvider(provider)
						.build();
				
				commandDelegator.send(new TestCommand());
			});
		}
		
		@Test
		public void shouldThrowWhenDuplicateCommandHandlersAreFound() {
			
			assertThrows(DuplicateCommandHandlerFoundException.class, () -> {
				CommandHandlerProvider provider = new CommandHandlerProvider() {
					@Override
					public <TCommand extends Command> Optional<CommandHandler<TCommand>> getCommandHandlerFor(
							Class<TCommand> commandType) {
						return Optional.of(new BaseCommandHandler<TCommand>());
					}
				};
				
				var compositeProvider = new CompositeCommandHandlerProvider(List.of(provider, provider));
				
				CommandDispatcher commandDelegator = CommandDispatcher
						.builder()
						.addCommandHandlerProvider(compositeProvider)
						.build();
				
				commandDelegator.send(new TestCommand());
			});
		}
	}
}
