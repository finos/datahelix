package com.scottlogic.deg.profile.reader.commands;

import com.scottlogic.deg.common.commands.CommandBase;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.profile.dtos.constraints.ConstraintDTO;

import java.util.List;

public class CreateConstraints extends CommandBase<List<Constraint>>
{
    public final List<ConstraintDTO> constraintDTOs;
    public final Fields fields;

    public CreateConstraints(List<ConstraintDTO> constraintDTOs, Fields fields)
    {
        this.constraintDTOs = constraintDTOs;
        this.fields = fields;
    }
}
