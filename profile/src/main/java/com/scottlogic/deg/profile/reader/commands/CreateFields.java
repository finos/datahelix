package com.scottlogic.deg.profile.reader.commands;

import com.scottlogic.deg.common.commands.Command;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.RuleDTO;

import java.util.List;

public class CreateFields extends Command<Fields>
{
    public final List<FieldDTO> fieldDTOs;
    public final List<RuleDTO> ruleDTOs;

    public CreateFields(List<FieldDTO> fieldDTOs, List<RuleDTO> ruleDTOs)
    {
        this.fieldDTOs = fieldDTOs;
        this.ruleDTOs = ruleDTOs;
    }
}
