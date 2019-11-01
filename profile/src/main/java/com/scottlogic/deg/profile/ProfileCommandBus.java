package com.scottlogic.deg.profile;

import com.google.inject.Inject;
import com.scottlogic.deg.common.commands.CommandBus;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.reader.commands.CreateProfile;
import com.scottlogic.deg.profile.reader.handlers.CreateProfileHandler;
import com.scottlogic.deg.profile.reader.services.FieldService;
import com.scottlogic.deg.profile.reader.services.RuleService;

public class ProfileCommandBus extends CommandBus
{
    @Inject
    public ProfileCommandBus(FieldService fieldService, RuleService ruleService, Validator<CreateProfile> validator)
    {
        register(CreateProfile.class, new CreateProfileHandler(fieldService, ruleService, validator));
    }
}
