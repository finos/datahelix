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

package com.scottlogic.datahelix.generator.common.commands;

import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.common.validators.Validator;

public abstract class CommandHandler<TCommand extends Command<TResponse>, TResponse>
{
    private final Validator<TCommand> validator;

    protected CommandHandler(Validator<TCommand> validator)
    {
        this.validator = validator;
    }

    public CommandResult<TResponse> handle(TCommand command)
    {
        ValidationResult validationResult = validator.validate(command);
        if (validationResult.isSuccess) return handleCommand(command);
        return CommandResult.failure(validationResult.errors);
    }

    public abstract CommandResult<TResponse> handleCommand(TCommand command);
}

