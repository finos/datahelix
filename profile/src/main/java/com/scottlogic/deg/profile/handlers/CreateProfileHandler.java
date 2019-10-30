package com.scottlogic.deg.profile.handlers;

import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.profile.commands.CreateProfile;

public class CreateProfileHandler extends CommandHandler<CreateProfile, Profile>
{
    public CreateProfileHandler(Validator<CreateProfile> validator)
    {
        super(validator);
    }

    @Override
    protected CommandResult<Profile> handleCommand(CreateProfile command)
    {
        return null;
    }
}
