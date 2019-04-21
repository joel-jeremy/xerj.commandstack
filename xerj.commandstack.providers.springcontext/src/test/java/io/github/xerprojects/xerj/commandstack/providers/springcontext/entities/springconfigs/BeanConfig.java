package io.github.xerprojects.xerj.commandstack.providers.springcontext.entities.springconfigs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.entities.TestCommand;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.entities.TestCommandHandler;

@Configuration
public class BeanConfig {
    @Bean
    public CommandHandler<TestCommand> getTestCommandHandler() {
        return new TestCommandHandler();  
    }
}