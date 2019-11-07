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

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public abstract class CommandBus
{
    private final Map<Class<?>, Pair<Class<? extends CommandHandler>, ? extends CommandHandler>> map = new HashMap<>();

    protected <TCommand extends Command<TResponse>, TResponse> void register(Class<TCommand> commandClass, CommandHandler handler)
    {
        map.put(commandClass, new ImmutablePair<>(handler.getClass(), handler));
    }

    public <TCommand extends Command<TResponse>, TResponse> CommandResult<TResponse> send(TCommand command)
    {
        Class<? extends Command> commandClass = command.getClass();
        Class<? extends CommandHandler> commandHandlerClass = map.get(commandClass).getKey();
        CommandHandler commandHandler = map.get(commandClass).getValue();

        return commandHandlerClass.cast(commandHandler).handle(command);
    }
}
