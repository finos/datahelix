package com.scottlogic.deg.profile.commands;

import com.scottlogic.deg.common.commands.CommandBase;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.RuleDTO;

import java.util.List;

public class CreateFields extends CommandBase<ProfileFields>
{
    public final List<FieldDTO> fieldDTOs;
    public final List<RuleDTO> ruleDTOs;

    public CreateFields(List<FieldDTO> fieldDTOs, List<RuleDTO> ruleDTOs)
    {
        this.fieldDTOs = fieldDTOs;
        this.ruleDTOs = ruleDTOs;
    }
}
