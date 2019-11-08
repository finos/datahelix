/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scottlogic.deg.common.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommandResult<T>
{
    public final T value;
    public final boolean isSuccess;
    public final List<String> errors;

    private CommandResult(T value, boolean isSuccess, List<String> errors)
    {
        this.value = value;
        this.isSuccess = isSuccess;
        this.errors = errors;
    }

    public static <T> CommandResult<T> success(T value)
    {
        return new CommandResult<>(value, true, new ArrayList<>());
    }

    @SafeVarargs
    public static <T> CommandResult<T> failure(List<String>... errors)
    {
        return new CommandResult<>(null, false, Arrays.stream(errors).flatMap(Collection::stream).collect(Collectors.toList()));
    }

    public <TResult> CommandResult<TResult> map(Function<T, TResult> mapping)
    {
        return isSuccess ? success(mapping.apply(value)) : failure(errors);
    }


    public static <T> CommandResult<List<T>> combine(List<CommandResult<T>> results)
    {
        List<T> values = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        results.forEach(result ->
        {
            if(result.isSuccess) values.add(result.value);
            else errors.addAll(result.errors);
        });

        return values.size() == results.size() ? CommandResult.success(values) : CommandResult.failure(errors);
    }
}
