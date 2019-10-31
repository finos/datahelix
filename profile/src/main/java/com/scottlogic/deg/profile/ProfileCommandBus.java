package com.scottlogic.deg.profile;

import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import com.google.inject.Inject;
import com.scottlogic.deg.common.commands.CommandBase;
import com.scottlogic.deg.common.commands.CommandBus;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.profile.reader.handlers.CreateConstraintsHandler;
import com.scottlogic.deg.profile.reader.handlers.CreateFieldsHandler;
import com.scottlogic.deg.profile.reader.handlers.CreateProfileHandler;
import com.scottlogic.deg.profile.reader.FileReader;
import com.scottlogic.deg.profile.reader.validators.CreateConstraintsValidator;
import com.scottlogic.deg.profile.reader.validators.CreateFieldsValidator;
import com.scottlogic.deg.profile.reader.validators.CreateProfileValidator;

import java.util.stream.Stream;

public class ProfileCommandBus implements CommandBus
{
    private final Pipeline pipeline;

    @Inject
    public ProfileCommandBus(FileReader fileReader)
    {
        pipeline = new Pipelinr(() -> Stream.of(
            new CreateProfileHandler(this, new CreateProfileValidator()),
            new CreateFieldsHandler(new CreateFieldsValidator()),
            new CreateConstraintsHandler(fileReader, new CreateConstraintsValidator())));
    }

    @Override
    public <TCommand extends CommandBase<TResponse>, TResponse> CommandResult<TResponse> send(TCommand command)
    {
        return pipeline.send(command);
    }
}
