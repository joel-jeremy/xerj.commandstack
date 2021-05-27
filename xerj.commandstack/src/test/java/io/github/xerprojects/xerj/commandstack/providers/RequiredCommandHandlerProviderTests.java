package io.github.xerprojects.xerj.commandstack.providers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.CommandHandlerProvider;
import io.github.xerprojects.xerj.commandstack.TestCommand;
import io.github.xerprojects.xerj.commandstack.exceptions.CommandHandlerNotFoundException;

@ExtendWith(MockitoExtension.class)
public class RequiredCommandHandlerProviderTests {
    @Nested
	public class Constructor {
		@Test
		@DisplayName("should throw when decorated command handler provider is null")
		public void test1() {
			assertThrows(IllegalArgumentException.class, () -> {
				new RequiredCommandHandlerProvider(null);
			});
		}
	}
	
	@Nested
	public class GetCommandHandlerForMethod {
		@Test
		@DisplayName("should provide registered command handler")
		public void test1(
				@Mock CommandHandlerProvider mockProvider,
				@Mock CommandHandler<TestCommand> mockHandler) {

			when(mockProvider.getCommandHandlerFor(TestCommand.class))
				.thenReturn(Optional.of(mockHandler));
                
            var requiredProvider = new RequiredCommandHandlerProvider(mockProvider);
			
			Optional<CommandHandler<TestCommand>> resolvedHandler = 
				requiredProvider.getCommandHandlerFor(TestCommand.class);			
			
			CommandHandler<TestCommand> handlerInstance = resolvedHandler.get();
			
			assertNotNull(handlerInstance);
			assertSame(mockHandler, handlerInstance);
        }
        
        @Test
		@DisplayName("should throw when no command handler is registered")
		public void test2(@Mock CommandHandlerProvider mockProvider) {

			when(mockProvider.getCommandHandlerFor(TestCommand.class))
				.thenReturn(Optional.empty());
            
            assertThrows(CommandHandlerNotFoundException.class, () -> {
                var requiredProvider = new RequiredCommandHandlerProvider(mockProvider);
                requiredProvider.getCommandHandlerFor(TestCommand.class);
            });
		}

		@Test
		@DisplayName("should throw when command type argument is null")
		public void test3(@Mock CommandHandlerProvider mockProvider) {
			assertThrows(IllegalArgumentException.class, () -> {
				var requiredProvider = new RequiredCommandHandlerProvider(mockProvider);
				requiredProvider.getCommandHandlerFor(null);
			});
		}
	}
}