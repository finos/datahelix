package com.scottlogic.deg.common.commands;

import an.awesome.pipelinr.Command;
import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.common.validators.Validator;

public abstract class CommandHandler<TCommand extends Command<CommandResult<TResponse>>, TResponse> implements Command.Handler<TCommand, CommandResult<TResponse>>
{
    private final Validator<TCommand> validator;

    protected CommandHandler(Validator<TCommand> validator)
    {
        this.validator = validator;
    }

    @Override
    public CommandResult<TResponse> handle(TCommand command)
    {
        ValidationResult validationResult = validator.validate(command);
        if(validationResult.isValid) return handleCommand(command);
        return CommandResult.failure(validationResult.errors);
    }

    protected abstract CommandResult<TResponse> handleCommand(TCommand command);
}

