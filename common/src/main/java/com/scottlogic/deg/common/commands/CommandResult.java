package com.scottlogic.deg.common.commands;

import java.util.ArrayList;
import java.util.List;

public class CommandResult<T>
{
    public final T value;
    public final boolean hasValue;
    public final List<String> errors;

    private CommandResult(T value, List<String> errors)
    {
        this.value = value;
        this.hasValue = value != null;
        this.errors = errors;
    }

    public static <T> CommandResult<T> success(T value)
    {
        return new CommandResult<>(value, new ArrayList<>());
    }

    public static <T> CommandResult<T> failure(List<String> errors)
    {
        return new CommandResult<>(null, errors);
    }
}
