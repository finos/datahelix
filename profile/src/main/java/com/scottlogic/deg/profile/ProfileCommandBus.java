package com.scottlogic.deg.profile;

import com.google.inject.Inject;
import com.scottlogic.deg.common.commands.CommandBase;
import com.scottlogic.deg.common.commands.CommandBus;
import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
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

import java.util.HashMap;
import java.util.Map;

public class ProfileCommandBus implements CommandBus
{
    private static final Map<Class, CommandHandler> HANDLERS = new HashMap<>();

    @Inject
    public ProfileCommandBus(FileReader fileReader)
    {
        HANDLERS.put(CreateProfile.class, new CreateProfileHandler(this, new CreateProfileValidator()));
        HANDLERS.put(CreateFields.class, new CreateFieldsHandler(new CreateFieldsValidator()));
        HANDLERS.put(CreateConstraints.class, new CreateConstraintsHandler(fileReader, new CreateConstraintsValidator()));
    }

    @Override
    public <TCommand extends CommandBase<TResponse>, TResponse> CommandResult<TResponse> send(TCommand command)
    {
        return ((CommandHandler<TCommand,TResponse>)HANDLERS.get(command.getClass())).handle(command);
    }
}
