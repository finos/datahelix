package com.scottlogic.deg.common.commands;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.common.validators.Validator;

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

