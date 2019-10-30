package com.scottlogic.deg.profile.handlers;

import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.profile.commands.CreateNonNullableRule;

public class CreateNonNullableRuleHandler extends CommandHandler<CreateNonNullableRule, Rule>
{
    public CreateNonNullableRuleHandler(Validator<CreateNonNullableRule> validator)
    {
        super(validator);
    }

    @Override
    protected CommandResult<Rule> handleCommand(CreateNonNullableRule command)
    {
        return null;
    }
}
