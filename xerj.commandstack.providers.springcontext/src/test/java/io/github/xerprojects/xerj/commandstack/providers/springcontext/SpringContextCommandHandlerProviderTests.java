package io.github.xerprojects.xerj.commandstack.providers.springcontext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.entities.AppContextConfig;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.entities.TestCommand;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.entities.TestCommandHandler;

public class SpringContextCommandHandlerProviderTests {
	@Nested
	public class Constructor {
		@Test
		public void shouldThrowWhenContainerAdapterArgumentIsNull() {
			assertThrows(IllegalArgumentException.class, () -> {
				new SpringContextCommandHandlerProvider(null);
			});
		}
	}
			
	@Nested
	public class GetCommandHandlerForMethod {
		@Test
		public void shouldProvideCommandHandlerFromContainer() {
			var appContext = new AnnotationConfigApplicationContext(AppContextConfig.class);
			var provider = new SpringContextCommandHandlerProvider(appContext);
			Optional<CommandHandler<TestCommand>> resolvedHandler = provider.getCommandHandlerFor(TestCommand.class);
			
			CommandHandler<TestCommand> instance = resolvedHandler.get();
			
			assertNotNull(instance);
			assertTrue(instance instanceof TestCommandHandler);
		}
		
		@Test
		public void shouldThrowWhenCommandClassArgumentIsNull() {
			assertThrows(IllegalArgumentException.class, () -> {
				var appContext = new AnnotationConfigApplicationContext(AppContextConfig.class);
				var provider = new SpringContextCommandHandlerProvider(appContext);
				provider.getCommandHandlerFor(null);
			});
		}
	}
}