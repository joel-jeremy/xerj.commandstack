package io.github.xerprojects.xerj.commandstack.providers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
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
import io.github.xerprojects.xerj.commandstack.exceptions.DuplicateCommandHandlerFoundException;

@ExtendWith(MockitoExtension.class)
public class CompositeCommandHandlerProviderTests {
    @Nested
	public class Constructor {
		@Test
		@DisplayName("should throw when provider list is null")
		public void test1() {
			assertThrows(IllegalArgumentException.class, () -> {
				new CompositeCommandHandlerProvider(null);
			});
		}

		@Test
		@DisplayName("should throw when provider list is empty")
		public void test2() {
			assertThrows(IllegalArgumentException.class, () -> {
				new CompositeCommandHandlerProvider(List.of());
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
                
            var compositeProvider = new CompositeCommandHandlerProvider(List.of(mockProvider));
			
			Optional<CommandHandler<TestCommand>> resolvedHandler = 
				compositeProvider.getCommandHandlerFor(TestCommand.class);			
			
			CommandHandler<TestCommand> handlerInstance = resolvedHandler.get();
			
			assertNotNull(handlerInstance);
			assertSame(mockHandler, handlerInstance);
        }
        
        @Test
		@DisplayName("should throw when command handler is in multiple providers")
		public void test2(
				@Mock CommandHandlerProvider mockProvider1,
				@Mock CommandHandlerProvider mockProvider2,
				@Mock CommandHandler<TestCommand> mockHandler) {

			when(mockProvider1.getCommandHandlerFor(TestCommand.class))
				.thenReturn(Optional.of(mockHandler));

			when(mockProvider2.getCommandHandlerFor(TestCommand.class))
				.thenReturn(Optional.of(mockHandler));
            
            assertThrows(DuplicateCommandHandlerFoundException.class, () -> {
                var compositeProvider = new CompositeCommandHandlerProvider(
					List.of(mockProvider1, mockProvider2));
                
                compositeProvider.getCommandHandlerFor(TestCommand.class);
            });
		}
		
		@Test
		@DisplayName("should propagate exceptions from providers")
		public void test3(@Mock CommandHandlerProvider mockProvider) {

			when(mockProvider.getCommandHandlerFor(TestCommand.class))
				.thenThrow(RuntimeException.class);
			
			assertThrows(RuntimeException.class, () -> {
				var compositeProvider = new CompositeCommandHandlerProvider(List.of(mockProvider));
				compositeProvider.getCommandHandlerFor(TestCommand.class);
			});
		}

		@Test
		@DisplayName("should throw when command type argument is null")
		public void test4(@Mock CommandHandlerProvider mockProvider) {
			assertThrows(IllegalArgumentException.class, () -> {
				var compositeProvider = new CompositeCommandHandlerProvider(List.of(mockProvider));
				compositeProvider.getCommandHandlerFor(null);
			});
		}
	}
}