package com.scottlogic.deg.profile.handlers;

import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.commands.CreateFields;

public class CreateFieldsHandler extends CommandHandler<CreateFields, ProfileFields>
{
    public CreateFieldsHandler(Validator<CreateFields> validator)
    {
        super(validator);
    }

    @Override
    protected CommandResult<ProfileFields> handleCommand(CreateFields command)
    {
        return null;
    }
}
