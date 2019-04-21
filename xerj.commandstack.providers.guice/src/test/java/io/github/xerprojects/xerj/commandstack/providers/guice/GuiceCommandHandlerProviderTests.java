package io.github.xerprojects.xerj.commandstack.providers.guice;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.providers.guice.entities.AppModule;
import io.github.xerprojects.xerj.commandstack.providers.guice.entities.TestCommand;
import io.github.xerprojects.xerj.commandstack.providers.guice.entities.TestCommandHandler;

public class GuiceCommandHandlerProviderTests {
	@Nested
	public class Constructor {
		@Test
		public void shouldThrowWhenInjectorArgumentIsNull() {
			assertThrows(IllegalArgumentException.class, () -> {
				new GuiceCommandHandlerProvider(null);
			});
		}
	}
			
	@Nested
	public class GetCommandHandlerForMethod {

		@Test
		public void shouldProvideCommandHandlerFromInjector() {
			Injector injector = Guice.createInjector(new AppModule());
			var provider = new GuiceCommandHandlerProvider(injector);
			Optional<CommandHandler<TestCommand>> resolvedHandler = provider.getCommandHandlerFor(TestCommand.class);
			
			CommandHandler<TestCommand> providerInstance = resolvedHandler.get();
			
			assertNotNull(providerInstance);
			assertTrue(providerInstance instanceof TestCommandHandler);
		}
		
		@Test
		public void shouldThrowWhenCommandClassArgumentIsNull() {
			assertThrows(IllegalArgumentException.class, () -> {
				Injector injector = Guice.createInjector(new AppModule());
				var provider = new GuiceCommandHandlerProvider(injector);
				provider.getCommandHandlerFor(null);
			});
		}
	}
}