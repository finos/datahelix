package com.scottlogic.deg.profile.commands;

import com.scottlogic.deg.common.commands.CommandBase;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.profile.Rule;

public class CreateNonNullableRule extends CommandBase<Rule>
{
    public final ProfileFields fields;

    public CreateNonNullableRule(ProfileFields fields)
    {
        this.fields = fields;
    }
}
