package io.github.xerprojects.xerj.commandstack.providers.registry;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.TestCommand;
import io.github.xerprojects.xerj.commandstack.TestCommandHandler;
import io.github.xerprojects.xerj.commandstack.exceptions.DuplicateCommandHandlerRegistrationException;

@ExtendWith(MockitoExtension.class)
public class RegistryCommandHandlerProviderTests {
	
	@Nested
	public class Constructor {
		@Test
		@DisplayName("should throw when registry configuration is null")
		public void test1() {
			assertThrows(IllegalArgumentException.class, () -> {
				new RegistryCommandHandlerProvider(null);
			});
		}
	}
	
	@Nested
	public class MapRegistryTests {
		
		@Nested
		public class RegisterCommandHandlerMethod {
			@Test
			@DisplayName("should throw when command type argument is null")
			public void test1() {
				assertThrows(IllegalArgumentException.class, () -> {
					new RegistryCommandHandlerProvider(config ->
						// Null.
						config.registerCommandHandler(null, () -> new TestCommandHandler()));
				});
			}
			
			@Test
			@DisplayName("should throw when instance factory argument is null")
			public void test2() {
				assertThrows(IllegalArgumentException.class, () -> {
					new RegistryCommandHandlerProvider(config ->
						// Null.
						config.registerCommandHandler(TestCommand.class, null));
				});
			}
			
			@Test
			@DisplayName("should throw when command is registered twice")
			public void test3() {
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
		@DisplayName("should provide registered command handler")
		public void test1(@Mock CommandHandler<TestCommand> mockHandler) {
			
			var provider = new RegistryCommandHandlerProvider(config ->
				config.registerCommandHandler(TestCommand.class, () -> mockHandler));
			
			Optional<CommandHandler<TestCommand>> resolvedHandler = 
				provider.getCommandHandlerFor(TestCommand.class);			
			
			CommandHandler<TestCommand> instance = resolvedHandler.get();
			
			assertNotNull(instance);
			assertSame(mockHandler, instance);
		}

		@Test
		@DisplayName("should return empty optional if no handler was registered")
		public void test2() {
			var provider = new RegistryCommandHandlerProvider(config -> {});
			Optional<CommandHandler<TestCommand>> handler = 
				provider.getCommandHandlerFor(TestCommand.class);

			assertTrue(handler.isEmpty());
		}
		
		@Test
		@DisplayName("should throw when command type argument is null")
		public void test3() {
			assertThrows(IllegalArgumentException.class, () -> {
				CommandHandler<TestCommand> testCommandHandler = new TestCommandHandler();
				var provider = new RegistryCommandHandlerProvider(config ->
					config.registerCommandHandler(TestCommand.class, () -> testCommandHandler));
					
				provider.getCommandHandlerFor(null);
			});
		}
	}
}
