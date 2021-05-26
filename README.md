# Table of contents
* [Overview](#overview)
* [Features](#features)
* [Installation](#installation)
* [Getting Started](#getting-started)

# Overview
Simple command handling library!

This project composes of components for implementing the command handling parts of the CQRS pattern. This library was built with simplicity, modularity and pluggability in mind.

## Features
* Send commands to registered command handlers.
* Multiple ways of registering command handlers:
    * Simple registration (no IoC container).
    * IoC container registration
      * achieved by creating implementations of CommandHandlerProvider: [See sample CommandHandlerProvider implementations](https://github.com/XerProjects/xerj.commandstack.samples/tree/main/sample-providers)
          
                    
    * Attribute registration (Soon!)
      * achieved by marking methods with @CommandHandler annotations.

## Installation

* XerJ.CommandStack is also available in the Maven Central:

    [![Maven Central](https://img.shields.io/maven-central/v/io.github.xerprojects/xerj.commandstack.svg?style=for-the-badge)](https://mvnrepository.com/artifact/io.github.xerprojects/xerj.commandstack)
   
   Maven:
   ```xml
   <dependency>
     <groupId>io.github.xerprojects</groupId>
     <artifactId>xerj.commandstack</artifactId>
     <version>${xerj.commandstack.version}</version>
   </dependency>
   ```
   
   Gradle:
   ```gradle
   implementation group: 'io.github.xerprojects', name: 'xerj.commandstack', version: $rootProject.commandStackVersion
   ```

## Getting Started

### Sample Command and Command Handler

Commands are just POJOs or if you are in a later java version, you can use records if you prefer.

```java
// Example POJO command.
public class RegisterProductCommand {
    private final int productId;
    private final String productName;

    public RegisterProductCommand(int productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }
}

// Command handler.
public class RegisterProductCommandHandler implements CommandHandler<RegisterProductCommand> {
    private final ProductRepository productRepository;

    public RegisterProductCommandHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void handle(RegisterProductCommand command) {
        validate(command);

        productRepository.save(new Product(command.getProductId(), command.getProductName()));
    }
}
```
### Command Handler Registration

Before we can dispatch any commands, first we need to register our command handlers. There are several ways to do this:

#### 1. Built-in (No dependency injection frameworks)
```java
public static void main(String[] args) {
    RegistryCommandHandlerProvider provider = new RegistryCommandHandlerProvider(registry -> {
        registry.registerCommandHandler(RegisterProductCommand.class, () -> 
            new RegisterProductCommandHandler(
                new InMemoryProductRepository()
            )
        );
    });

    CommandDispatcher dispatcher = new CommandStackDispatcher(provider);
    
    // Dispatch command.
    dispatcher.send(new RegisterProductCommand(1, "My Product Name"));
}
```

#### 2. Dependency Injection Frameworks

- Spring Context - See [Sample Spring Context Command Handler Provider](https://github.com/XerProjects/xerj.commandstack.samples/tree/main/sample-providers/sample-springcontext-provider)

- Guice - See [Sample Guice Command Handler Provider](https://github.com/XerProjects/xerj.commandstack.samples/tree/main/sample-providers/sample-guice-provider)

- Dagger - See [Sample Dagger Command Handler Provider](https://github.com/XerProjects/xerj.commandstack.samples/tree/main/sample-providers/sample-dagger-provider)


### Async Dispatch

Async dispatch is supported by decorating the `CommandStackDispatcher` with `AsyncCommandDispatcher`:
```java
public static void main(String[] args) {
   ExecutorService executor = Executors.newWorkStealingPool();
   
   CommandHandlerProvider commandHandlerProvider = getCommandHandlerProvider();
   
   CommandDispatcher dispatcher = new AsyncCommandDispatcher(
      new CommandStackDispatcher(commandHandlerProvider),
      executor);
      
   dispatcher.send(new RegisterProductCommand(1, "My Product Name"));
}
```
