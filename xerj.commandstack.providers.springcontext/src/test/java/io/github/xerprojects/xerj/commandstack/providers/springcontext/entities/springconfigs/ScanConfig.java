package io.github.xerprojects.xerj.commandstack.providers.springcontext.entities.springconfigs;

import org.springframework.context.annotation.Configuration;

import io.github.xerprojects.xerj.commandstack.providers.springcontext.ScanCommandHandlers;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.entities.BaseCommandHandler;

@Configuration
@ScanCommandHandlers(BaseCommandHandler.class)
public class ScanConfig {
    
}