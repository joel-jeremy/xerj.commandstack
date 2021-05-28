package io.github.xerprojects.xerj.commandstack.providers.springcontext.testentities.springconfigs;

import org.springframework.context.annotation.Configuration;

import io.github.xerprojects.xerj.commandstack.providers.springcontext.ScanCommandHandlers;
import io.github.xerprojects.xerj.commandstack.providers.springcontext.testentities.other.OtherTestCommandHandler;

@Configuration
@ScanCommandHandlers(OtherTestCommandHandler.class)
public class OtherScanConfig {
    
}
