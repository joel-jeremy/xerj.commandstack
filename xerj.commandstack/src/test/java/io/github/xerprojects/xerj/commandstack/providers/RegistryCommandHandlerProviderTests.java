package io.github.xerprojects.xerj.commandstack.providers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.entities.TestCommand;
import io.github.xerprojects.xerj.commandstack.entities.TestCommandHandler;
import io.github.xerprojects.xerj.commandstack.exceptions.DuplicateCommandHandlerRegistrationException;
import io.github.xerprojects.xerj.commandstack.providers.RegistryCommandHandlerProvider;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class RegistryCommandHandlerProviderTests {
	
	@Nested
	public class Constructor {
		@Test
		public void shouldThrowWhenConfigLambdaArgumentIsNull() {
			assertThrows(IllegalArgumentException.class, () -> {
				new RegistryCommandHandlerProvider(null);
			});
		}
	}
	
	@Nested
	public class InProcessConfigTests {
		
		@Nested
		public class RegisterCommandHandlerMethod {
			@Test
			public void shouldThrowWhenCommandTypeArgumentIsNull() {
				assertThrows(IllegalArgumentException.class, () -> {
					new RegistryCommandHandlerProvider(config ->
						// Null.
						config.registerCommandHandler(null, () -> new TestCommandHandler()));
				});
			}
			
			@Test
			public void shouldThrowWhenCommandHandlerInstanceFactoryArgumentIsNull() {
				assertThrows(IllegalArgumentException.class, () -> {
					new RegistryCommandHandlerProvider(config ->
						// Null.
						config.registerCommandHandler(TestCommand.class, null));
				});
			}
			
			@Test
			public void shouldThrowWhenCommandIsRegisteredTwice() {
				assertThrows(DuplicateCommandHandlerRegistrationException.class, () -> {
					@SuppressWarnings("unused")
					var provider = new RegistryCommandHandlerProvider(config ->
						// Register the same command twice.
						config.registerCommandHandler(TestCommand.class, () -> new TestCommandHandler())
							.registerCommandHandler(TestCommand.class, () -> new TestCommandHandler())
					);
				});
			}
		}
	}
	
	@Nested
	public class GetCommandHandlerForMethod {
		@Test
		public void shouldProvideRegisteredCommandHandler() {
			CommandHandler<TestCommand> testCommandHandler = new TestCommandHandler();
			
			var provider = new RegistryCommandHandlerProvider(config ->
				config.registerCommandHandler(TestCommand.class, () -> testCommandHandler));
			
			Optional<CommandHandler<TestCommand>> resolvedHandler = provider.getCommandHandlerFor(TestCommand.class);			
			
			CommandHandler<TestCommand> instance = resolvedHandler.get();
			
			assertNotNull(instance);
			assertSame(testCommandHandler, instance);
		}
		
		@Test
		public void shouldThrowWhenCommandClassArgumentIsNull() {
			assertThrows(IllegalArgumentException.class, () -> {
				CommandHandler<TestCommand> testCommandHandler = new TestCommandHandler();
				var provider = new RegistryCommandHandlerProvider(config ->
					config.registerCommandHandler(TestCommand.class, () -> testCommandHandler));
					
				provider.getCommandHandlerFor(null);
			});
		}
	}
}
