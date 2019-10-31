package com.scottlogic.deg.common.commands;

public abstract class CommandBase<TResponse>
{
    public String commandName;

    protected CommandBase()
    {
        commandName = getClass().getName();
    }
}
