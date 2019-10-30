package com.scottlogic.deg.profile.handlers;

import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.profile.commands.CreateRule;

public class CreateRuleHandler extends CommandHandler<CreateRule, Rule>
{
    public CreateRuleHandler(Validator<CreateRule> validator)
    {
        super(validator);
    }

    @Override
    protected CommandResult<Rule> handleCommand(CreateRule command)
    {
        return null;
    }
}
