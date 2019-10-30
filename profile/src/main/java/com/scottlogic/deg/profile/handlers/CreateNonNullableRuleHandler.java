package com.scottlogic.deg.profile.handlers;

import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.profile.commands.CreateNonNullableRule;

import java.util.Optional;

public class CreateNonNullableRuleHandler extends CommandHandler<CreateNonNullableRule, Optional<Rule>>
{
    public CreateNonNullableRuleHandler(Validator<CreateNonNullableRule> validator)
    {
        super(validator);
    }

    @Override
    protected CommandResult<Optional<Rule>> handleCommand(CreateNonNullableRule command)
    {
        return null;
    }
}
