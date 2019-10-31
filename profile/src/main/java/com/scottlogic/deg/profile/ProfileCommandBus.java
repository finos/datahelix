package com.scottlogic.deg.profile;

import com.google.inject.Inject;
import com.scottlogic.deg.common.commands.CommandBus;
import com.scottlogic.deg.profile.reader.FileReader;
import com.scottlogic.deg.profile.reader.commands.CreateConstraints;
import com.scottlogic.deg.profile.reader.commands.CreateFields;
import com.scottlogic.deg.profile.reader.commands.CreateProfile;
import com.scottlogic.deg.profile.reader.handlers.CreateConstraintsHandler;
import com.scottlogic.deg.profile.reader.handlers.CreateFieldsHandler;
import com.scottlogic.deg.profile.reader.handlers.CreateProfileHandler;
import com.scottlogic.deg.profile.reader.validators.CreateConstraintsValidator;
import com.scottlogic.deg.profile.reader.validators.CreateFieldsValidator;
import com.scottlogic.deg.profile.reader.validators.CreateProfileValidator;

public class ProfileCommandBus extends CommandBus
{
    @Inject
    public ProfileCommandBus(FileReader fileReader)
    {
        register(CreateProfile.class, new CreateProfileHandler(this, new CreateProfileValidator()));
        register(CreateFields.class, new CreateFieldsHandler(new CreateFieldsValidator()));
        register(CreateConstraints.class, new CreateConstraintsHandler(fileReader, new CreateConstraintsValidator()));
    }
}
