package com.xerprojects.xerj.commandstack;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import com.xerprojects.xerj.commandstack.entities.TestCommand;
import com.xerprojects.xerj.commandstack.entities.TestCommandHandler;
import com.xerprojects.xerj.commandstack.exceptions.DuplicateCommandHandlerFoundException;
import com.xerprojects.xerj.commandstack.exceptions.DuplicateCommandHandlerRegistrationException;
import com.xerprojects.xerj.commandstack.providers.CompositeCommandHandlerProvider;
import com.xerprojects.xerj.commandstack.providers.InProcessCommandHandlerProvider;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class CompositeCommandHandlerProviderTests {
    @Nested
	public class Constructor {
		@Test
		public void shouldThrowWhenConfigLambdaArgumentIsNull() {
			assertThrows(IllegalArgumentException.class, () -> {
				@SuppressWarnings("unused")
				CommandHandlerProvider provider = new CompositeCommandHandlerProvider(null);
			});
		}
	}
	
	@Nested
	public class GetCommandHandlerForMethod {
		@Test
		public void shouldProvideRegisteredCommandHandler() {
			CommandHandler<TestCommand> testCommandHandler = new TestCommandHandler();
			
			var provider1 = new InProcessCommandHandlerProvider(config ->
                config.registerCommandHandler(TestCommand.class, () -> testCommandHandler));
                
            var compositeProvider = new CompositeCommandHandlerProvider(List.of(provider1));
			
			Optional<CommandHandler<TestCommand>> resolvedHandler = compositeProvider.getCommandHandlerFor(TestCommand.class);			
			
			CommandHandler<TestCommand> instance = resolvedHandler.get();
			
			assertNotNull(instance);
			assertSame(testCommandHandler, instance);
        }
        
        @Test
		public void shouldThrowIfCommandHandlerIsRegisteredInMultipleProviders() {
            
            assertThrows(DuplicateCommandHandlerFoundException.class, () -> {
                var provider1 = new InProcessCommandHandlerProvider(config ->
                    config.registerCommandHandler(TestCommand.class, TestCommandHandler::new));
                
                var provider2 = new InProcessCommandHandlerProvider(config ->
                    config.registerCommandHandler(TestCommand.class, TestCommandHandler::new));
                    
                var compositeProvider = new CompositeCommandHandlerProvider(List.of(provider1, provider2));
                
                compositeProvider.getCommandHandlerFor(TestCommand.class);
            });
		}
	}
}