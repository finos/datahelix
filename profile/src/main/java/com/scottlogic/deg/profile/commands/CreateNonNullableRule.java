package com.scottlogic.deg.profile.commands;

import com.scottlogic.deg.common.commands.CommandBase;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.generator.profile.Rule;

import java.util.Optional;

public class CreateNonNullableRule extends CommandBase<Optional<Rule>>
{
    public final Fields fields;

    public CreateNonNullableRule(Fields fields)
    {
        this.fields = fields;
    }
}
