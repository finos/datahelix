package com.scottlogic.deg.profile.reader;

import com.google.inject.Inject;
import com.scottlogic.deg.common.commands.CommandBus;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.creation.commands.CreateProfile;
import com.scottlogic.deg.profile.creation.handlers.CreateProfileHandler;
import com.scottlogic.deg.profile.creation.services.FieldService;
import com.scottlogic.deg.profile.creation.services.RuleService;

public class ProfileCommandBus extends CommandBus
{
    @Inject
    public ProfileCommandBus(FieldService fieldService, RuleService ruleService, Validator<CreateProfile> validator)
    {
        register(CreateProfile.class, new CreateProfileHandler(fieldService, ruleService, validator));
    }
}
