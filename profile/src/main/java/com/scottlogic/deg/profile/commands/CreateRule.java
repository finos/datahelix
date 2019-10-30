package com.scottlogic.deg.profile.commands;

import com.scottlogic.deg.common.commands.CommandBase;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.profile.dtos.RuleDTO;

public class CreateRule extends CommandBase<Rule>
{
    public final RuleDTO dto;

    public CreateRule(RuleDTO dto)
    {
        this.dto = dto;
    }
}
