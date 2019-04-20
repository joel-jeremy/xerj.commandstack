package io.github.xerprojects.xerj.commandstack.providers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.entities.TestCommand;
import io.github.xerprojects.xerj.commandstack.entities.TestCommandHandler;
import io.github.xerprojects.xerj.commandstack.exceptions.CommandHandlerNotFoundException;

public class RequiredCommandHandlerProviderTests {
    @Nested
	public class Constructor {
		@Test
		public void shouldThrowWhenConfigLambdaArgumentIsNull() {
			assertThrows(IllegalArgumentException.class, () -> {
				new RequiredCommandHandlerProvider(null);
			});
		}
	}
	
	@Nested
	public class GetCommandHandlerForMethod {
		@Test
		public void shouldProvideRegisteredCommandHandler() {
			CommandHandler<TestCommand> testCommandHandler = new TestCommandHandler();
			
			var provider = new RegistryCommandHandlerProvider(config ->
                config.registerCommandHandler(TestCommand.class, () -> testCommandHandler));
                
            var requiredProvider = new RequiredCommandHandlerProvider(provider);
			
			Optional<CommandHandler<TestCommand>> resolvedHandler = requiredProvider.getCommandHandlerFor(TestCommand.class);			
			
			CommandHandler<TestCommand> instance = resolvedHandler.get();
			
			assertNotNull(instance);
			assertSame(testCommandHandler, instance);
        }
        
        @Test
		public void shouldThrowWhenCommandHandlerIsNotRegistered() {
            
            assertThrows(CommandHandlerNotFoundException.class, () -> {
                var provider = new RegistryCommandHandlerProvider(config -> {});
                    
                var requiredProvider = new RequiredCommandHandlerProvider(provider);
                
                requiredProvider.getCommandHandlerFor(TestCommand.class);
            });
		}

		@Test
		public void shouldThrowWhenCommandClassArgumentIsNull() {
			assertThrows(IllegalArgumentException.class, () -> {
                var provider = new RegistryCommandHandlerProvider(config ->
					config.registerCommandHandler(TestCommand.class, TestCommandHandler::new));
					
				var requiredProvider = new RequiredCommandHandlerProvider(provider);
				
				requiredProvider.getCommandHandlerFor(null);
			});
		}
	}
}