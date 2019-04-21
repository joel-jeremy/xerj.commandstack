package io.github.xerprojects.xerj.commandstack.providers.springcontext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.exceptions.DuplicateCommandHandlerFoundException;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.entities.TestCommand;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.entities.TestCommandHandler;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.entities.springconfigs.AppContextConfig;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.entities.springconfigs.DuplicateHandlerConfig;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.entities.springconfigs.NullConfig;

public class SpringContextCommandHandlerProviderTests {
	@Nested
	public class Constructor {
		@Test
		public void shouldThrowWhenApplicationContextArgumentIsNull() {
			assertThrows(IllegalArgumentException.class, () -> {
				new SpringContextCommandHandlerProvider(null);
			});
		}
	}
			
	@Nested
	public class GetCommandHandlerForMethod {
		@Test
		public void shouldProvideCommandHandlerFromApplicationContext() {
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
		
		@Test
		public void shouldThrowWhenDuplicateCommandHandlerIsFound() {
			assertThrows(DuplicateCommandHandlerFoundException.class, () -> {
				var appContext = new AnnotationConfigApplicationContext(DuplicateHandlerConfig.class);
				var provider = new SpringContextCommandHandlerProvider(appContext);
				provider.getCommandHandlerFor(TestCommand.class);
			});
		}

		@Test
		public void shouldReturnEmptyOptionalIfNoCommandHandlerIsFound() {
			var appContext = new AnnotationConfigApplicationContext(NullConfig.class);
			var provider = new SpringContextCommandHandlerProvider(appContext);
			Optional<CommandHandler<TestCommand>> resolvedHandler = provider.getCommandHandlerFor(TestCommand.class);
						
			assertNotNull(resolvedHandler);
			assertTrue(!resolvedHandler.isPresent());
		}
	}
}