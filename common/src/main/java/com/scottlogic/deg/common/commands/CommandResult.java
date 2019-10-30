package com.scottlogic.deg.common.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    @SafeVarargs
    public static <T> CommandResult<T> failure(List<String>... errors)
    {
        return new CommandResult<>(null, Arrays.stream(errors).flatMap(Collection::stream).collect(Collectors.toList()));
    }


    public static <T> CommandResult<List<T>> combine(List<CommandResult<T>> results)
    {
        List<T> values = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        results.forEach(result ->
        {
            if(result.hasValue) values.add(result.value);
            else errors.addAll(result.errors);
        });

        return values.size() == results.size() ? CommandResult.success(values) : CommandResult.failure(errors);
    }
}
