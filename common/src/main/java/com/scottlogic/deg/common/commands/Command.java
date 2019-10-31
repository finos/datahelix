package com.scottlogic.deg.common.commands;

import java.lang.reflect.Type;

public abstract class Command<TResponse>
{
    public Type commandType;

    protected Command()
    {
        commandType = getClass();
    }
}
