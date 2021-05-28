package io.github.xerprojects.xerj.commandstack.providers.springcontext.testentities.springconfigs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.xerprojects.xerj.commandstack.CommandHandler;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.testentities.TestCommand;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.testentities.TestCommandHandler;

@Configuration
public class DuplicateHandlerConfig {
    @Bean
    public CommandHandler<TestCommand> getTestCommandHandler1() {
        return new TestCommandHandler();  
    }

    @Bean
    public CommandHandler<TestCommand> getTestCommandHandler2() {
        return new TestCommandHandler();  
    }
}