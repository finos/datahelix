package com.scottlogic.deg.common.commands;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public abstract class CommandBus
{
    private final Map<Class<?>, Pair<Class<? extends CommandHandler>, ? extends CommandHandler>> map = new HashMap<>();

    protected <TCommand extends Command<TResponse>, TResponse> void register(Class<TCommand> commandClass, CommandHandler handler)
    {
        map.put(commandClass, new Pair<>(handler.getClass(), handler));
    }

    public <TCommand extends Command<TResponse>, TResponse> CommandResult<TResponse> send(TCommand command)
    {
        Class<? extends Command> commandClass = command.getClass();
        Class<? extends CommandHandler> commandHandlerClass = map.get(commandClass).getKey();
        CommandHandler commandHandler = map.get(commandClass).getValue();

        return commandHandlerClass.cast(commandHandler).handle(command);
    }
}
