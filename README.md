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
      * achieved by creating implementations of CommandHandlerProvider:
        * Spring Context
          
          [![Maven Central](https://img.shields.io/maven-central/v/io.github.xerprojects/xerj.commandstack.providers.springcontext.svg?style=for-the-badge)](https://mvnrepository.com/artifact/io.github.xerprojects/xerj.commandstack.providers.springcontext)
          
        * Guice
          
          [![Maven Central](https://img.shields.io/maven-central/v/io.github.xerprojects/xerj.commandstack.providers.guice.svg?style=for-the-badge)](https://mvnrepository.com/artifact/io.github.xerprojects/xerj.commandstack.providers.guice)
          
                    
    * Attribute registration (Soon!)
      * achieved by marking methods with @CommandHandler annotations.

## Installation
* You can simply clone this repository, build the source, reference the jar from your project, and code away!

* XerJ.CommandStack is also available in the Maven Central:

    [![Maven Central](https://img.shields.io/maven-central/v/io.github.xerprojects/xerj.commandstack.svg?style=for-the-badge)](https://mvnrepository.com/artifact/io.github.xerprojects/xerj.commandstack)

## Getting Started

### Sample Command and Command Handler

```java
// Example command.
public class RegisterProductCommand
{
    private final int productId;
    private final String productName;

    public RegisterProductCommand(int productId, String productName) 
    {
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
public class RegisterProductCommandHandler : CommandHandler<RegisterProductCommand>
{
    private final ProductRepository productRepository;

    public RegisterProductCommandHandler(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    @Override
    public CompletableFuture<Void> handle(RegisterProductCommand command)
    {
        return productRepository.save(new Product(command.getProductId(), command.getProductName()));
    }
}
```
### Command Handler Registration

Before we can dispatch any commands, first we need to register our command handlers. There are several ways to do this:

#### 1. Simple Registration (No IoC container)
```java
public static void main(String[] args)
{
    RegistryCommandHandlerProvider provider = new RegistryCommandHandlerProvider(registry -> {
        registry.registerCommandHandler(RegisterProductCommand.class, () -> new RegisterProductCommandHandler(
            new InMemoryProductRepository()
        ));
    });

    CommandDispatcher dispatcher = new CommandDispatcher(provider);
    
    // Dispatch command.
    CompletableFuture<Void> future = dispatcher.send(new RegisterProductCommand(1, "My Product Name"));
}
```

#### 2. Container Registration

Spring Context
```java
public static void main(String[] args)
{ 
    ApplicationContext appContext = new AnnotationConfigApplicationContext(BeanConfigs.class);

    SpringContextCommandHandlerPovider provider = new SpringContextCommandHandlerProvider(appContext);

    CommandDispatcher dispatcher = new CommandDispatcher(provider);

    // Dispatch command.
    CompletableFuture<Void> future = dispatcher.send(new RegisterProductCommand(1, "My Product Name"));
}
```

Guice
```java
public static void main(String[] args)
{ 
    Injector injector = Guice.createInjector(new AppModule());

    GuiceCommandHandlerPovider provider = new GuiceCommandHandlerProvider(injector);

    CommandDispatcher dispatcher = new CommandDispatcher(provider);

    // Dispatch command.
    CompletableFuture<Void> future = dispatcher.send(new RegisterProductCommand(1, "My Product Name"));
}
```