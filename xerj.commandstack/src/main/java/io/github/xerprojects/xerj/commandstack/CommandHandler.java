package io.github.xerprojects.xerj.commandstack;

import java.util.concurrent.CompletableFuture;

public interface CommandHandler<TCommand extends Command> {
	CompletableFuture<Void> handle(TCommand command);
}
