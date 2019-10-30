package com.scottlogic.deg.profile.commands;

import com.scottlogic.deg.common.commands.CommandBase;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.generator.profile.Rule;

import java.util.Optional;

public class CreateSpecificTypesRule extends CommandBase<Optional<Rule>>
{
    public final Fields fields;

    public CreateSpecificTypesRule(Fields fields)
    {
        this.fields = fields;
    }
}
