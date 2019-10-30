package com.scottlogic.deg.common.commands;

import an.awesome.pipelinr.Command;

public abstract class CommandBase<TResponse> implements Command<CommandResult<TResponse>>
{
    public String commandName;

    protected CommandBase()
    {
        commandName = getClass().getName();
    }
}
