package com.scottlogic.deg.profile.handlers;

import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.commands.CreateField;

public class CreateFieldHandler extends CommandHandler<CreateField, Field>
{
    public CreateFieldHandler(Validator<CreateField> validator)
    {
        super(validator);
    }

    @Override
    protected CommandResult<Field> handleCommand(CreateField command)
    {
        return null;
    }
}
