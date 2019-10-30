package com.scottlogic.deg.profile.commands;

import com.scottlogic.deg.common.commands.CommandBase;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.profile.dtos.constraints.ConstraintDTO;

public class CreateConstraint extends CommandBase<Constraint>
{
    public final ConstraintDTO dto;

    public CreateConstraint(ConstraintDTO dto)
    {
        this.dto = dto;
    }
}
