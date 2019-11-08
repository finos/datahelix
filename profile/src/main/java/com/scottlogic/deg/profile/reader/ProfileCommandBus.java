package com.scottlogic.deg.profile.reader;

import com.google.inject.Inject;
import com.scottlogic.deg.common.commands.CommandBus;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.commands.CreateProfile;
import com.scottlogic.deg.profile.handlers.CreateProfileHandler;
import com.scottlogic.deg.profile.services.FieldService;
import com.scottlogic.deg.profile.services.RuleService;

public class ProfileCommandBus extends CommandBus
{
    @Inject
    public ProfileCommandBus(FieldService fieldService, RuleService ruleService, Validator<CreateProfile> validator)
    {
        register(CreateProfile.class, new CreateProfileHandler(fieldService, ruleService, validator));
    }
}
