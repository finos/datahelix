package com.scottlogic.deg.common.commands;

public interface CommandBus
{
    <TCommand extends CommandBase<TResponse>, TResponse> CommandResult<TResponse> send(TCommand command);
}
