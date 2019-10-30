package com.scottlogic.deg.profile;

import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import com.google.inject.Inject;
import com.scottlogic.deg.common.commands.CommandBase;
import com.scottlogic.deg.common.commands.CommandBus;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.profile.handlers.CreateFieldHandler;
import com.scottlogic.deg.profile.handlers.CreateFieldsHandler;
import com.scottlogic.deg.profile.handlers.CreateProfileHandler;
import com.scottlogic.deg.profile.handlers.CreateRuleHandler;
import com.scottlogic.deg.profile.reader.ConstraintReader;
import com.scottlogic.deg.profile.validators.CreateFieldValidator;
import com.scottlogic.deg.profile.validators.CreateFieldsValidator;
import com.scottlogic.deg.profile.validators.CreateProfileValidator;
import com.scottlogic.deg.profile.validators.CreateRuleValidator;

import java.util.stream.Stream;

public class ProfileCommandBus implements CommandBus
{
    private final Pipeline pipeline;

    @Inject
    public ProfileCommandBus(ConstraintReader constraintReader)
    {
        pipeline = new Pipelinr(() -> Stream.of(
            new CreateProfileHandler(this, new CreateProfileValidator()),
            new CreateRuleHandler(constraintReader, new CreateRuleValidator()) ,
            new CreateFieldsHandler(this, new CreateFieldsValidator()),
            new CreateFieldHandler(new CreateFieldValidator())));
    }

    @Override
    public <TCommand extends CommandBase<TResponse>, TResponse> CommandResult<TResponse> send(TCommand command)
    {
        return pipeline.send(command);
    }
}
