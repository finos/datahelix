package com.scottlogic.deg.profile;

import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import com.google.inject.Inject;
import com.scottlogic.deg.common.commands.CommandBase;
import com.scottlogic.deg.common.commands.CommandBus;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.profile.handlers.CreateFieldsHandler;
import com.scottlogic.deg.profile.handlers.CreateProfileHandler;
import com.scottlogic.deg.profile.reader.FileReader;
import com.scottlogic.deg.profile.validators.CreateFieldsValidator;
import com.scottlogic.deg.profile.validators.CreateProfileValidator;

import java.util.stream.Stream;

public class ProfileCommandBus implements CommandBus
{
    private final Pipeline pipeline;

    @Inject
    public ProfileCommandBus(FileReader fileReader)
    {
        pipeline = new Pipelinr(() -> Stream.of(
            new CreateProfileHandler(fileReader, this, new CreateProfileValidator()),
            new CreateFieldsHandler(new CreateFieldsValidator())));
    }

    @Override
    public <TCommand extends CommandBase<TResponse>, TResponse> CommandResult<TResponse> send(TCommand command)
    {
        return pipeline.send(command);
    }
}
