package io.github.xerprojects.xerj.commandstack.providers.springcontext.testentities.springconfigs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.testentities.TestCommand;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.testentities.TestCommandHandler;

@Configuration
public class BeanConfig {
    @Bean
    public CommandHandler<TestCommand> getTestCommandHandler() {
        return new TestCommandHandler();  
    }
}