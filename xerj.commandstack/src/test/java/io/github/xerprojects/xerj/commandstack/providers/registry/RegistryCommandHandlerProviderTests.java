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
import io.github.xerprojects.xerj.commandstack.exceptions.DuplicateCommandHandlerRegistrationException;
import io.github.xerprojects.xerj.commandstack.testentities.TestCommand;

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
			@SuppressWarnings("exports")
			public void test1(
					@Mock(stubOnly = true) CommandHandler<TestCommand> stubHandler) {

				assertThrows(IllegalArgumentException.class, () -> {
					new RegistryCommandHandlerProvider(config ->
						// Null.
						config.registerCommandHandler(null, () -> stubHandler));
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
			@SuppressWarnings("exports")
			public void test3(
					@Mock(stubOnly = true) CommandHandler<TestCommand> stubHandler1,
					@Mock(stubOnly = true) CommandHandler<TestCommand> stubHandler2) {

				assertThrows(DuplicateCommandHandlerRegistrationException.class, () -> {
					new RegistryCommandHandlerProvider(config ->
						// Register the same command twice.
						config.registerCommandHandler(TestCommand.class, () -> stubHandler1)
							.registerCommandHandler(TestCommand.class, () -> stubHandler2)
					);
				});
			}
		}
	}
	
	@Nested
	public class GetCommandHandlerForMethod {
		@Test
		@DisplayName("should provide registered command handler")
		@SuppressWarnings("exports")
		public void test1(
				@Mock(stubOnly = true) CommandHandler<TestCommand> stubHandler) {
			
			var provider = new RegistryCommandHandlerProvider(config ->
				config.registerCommandHandler(TestCommand.class, () -> stubHandler));
			
			Optional<CommandHandler<TestCommand>> resolvedHandler = 
				provider.getCommandHandlerFor(TestCommand.class);			
			
			CommandHandler<TestCommand> resolvedHandlerInstance = resolvedHandler.get();
			
			assertNotNull(resolvedHandlerInstance);
			assertSame(stubHandler, resolvedHandlerInstance);
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
		@SuppressWarnings("exports")
		public void test3(
				@Mock(stubOnly = true) CommandHandler<TestCommand> stubHandler) {
			
			assertThrows(IllegalArgumentException.class, () -> {
				var provider = new RegistryCommandHandlerProvider(config ->
					config.registerCommandHandler(TestCommand.class, () -> stubHandler));
					
				provider.getCommandHandlerFor(null);
			});
		}
	}
}
