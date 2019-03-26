package com.xerprojects.xerj.commandstack;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import com.xerprojects.xerj.commandstack.entities.TestCommand;
import com.xerprojects.xerj.commandstack.entities.TestCommandHandler;
import com.xerprojects.xerj.commandstack.exceptions.DuplicateCommandHandlerRegistrationException;
import com.xerprojects.xerj.commandstack.providers.InProcessCommandHandlerProvider;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class InProcessCommandHandlerProviderTests {
	
	@Nested
	public class Constructor {
		@Test
		public void shouldThrowWhenConfigLambdaArgumentIsNull() {
			assertThrows(IllegalArgumentException.class, () -> {
				@SuppressWarnings("unused")
				CommandHandlerProvider provider = new InProcessCommandHandlerProvider(null);
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
					new InProcessCommandHandlerProvider(config ->
						// Null.
						config.registerCommandHandler(null, () -> new TestCommandHandler()));
				});
			}
			
			@Test
			public void shouldThrowWhenCommandHandlerInstanceFactoryArgumentIsNull() {
				assertThrows(IllegalArgumentException.class, () -> {
					new InProcessCommandHandlerProvider(config ->
						// Null.
						config.registerCommandHandler(TestCommand.class, null));
				});
			}
			
			@Test
			public void shouldThrowWhenCommandIsRegisteredTwice() {
				assertThrows(DuplicateCommandHandlerRegistrationException.class, () -> {
					@SuppressWarnings("unused")
					CommandHandlerProvider provider = new InProcessCommandHandlerProvider(config ->
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
			
			var provider = new InProcessCommandHandlerProvider(config ->
				config.registerCommandHandler(TestCommand.class, () -> testCommandHandler));
			
			Optional<CommandHandler<TestCommand>> resolvedHandler = provider.getCommandHandlerFor(TestCommand.class);			
			
			CommandHandler<TestCommand> instance = resolvedHandler.get();
			
			assertNotNull(instance);
			assertSame(testCommandHandler, instance);
		}
	}
}
