package io.github.xerprojects.xerj.commandstack.providers.springcontext.entities;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.entities.TestCommand;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.entities.TestCommandHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppContextConfig {
    @Bean
    public CommandHandler<TestCommand> getTestCommandHandler() {
        return new TestCommandHandler();  
    }
}