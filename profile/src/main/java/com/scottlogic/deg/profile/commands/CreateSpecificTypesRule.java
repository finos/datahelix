package com.scottlogic.deg.profile.commands;

import com.scottlogic.deg.common.commands.CommandBase;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.profile.Rule;

public class CreateSpecificTypesRule extends CommandBase<Rule>
{
    public final ProfileFields fields;

    public CreateSpecificTypesRule(ProfileFields fields)
    {
        this.fields = fields;
    }
}
