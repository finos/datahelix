package com.scottlogic.deg.profile.handlers;

import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.profile.commands.CreateConstraint;

public class CreateConstraintHandler extends CommandHandler<CreateConstraint, Constraint>
{
    public CreateConstraintHandler(Validator<CreateConstraint> validator)
    {
        super(validator);
    }

    @Override
    protected CommandResult<Constraint> handleCommand(CreateConstraint command)
    {
        return null;
    }
}
