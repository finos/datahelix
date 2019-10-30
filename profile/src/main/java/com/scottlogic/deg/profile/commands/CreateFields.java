package com.scottlogic.deg.profile.commands;

import com.scottlogic.deg.common.commands.CommandBase;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.profile.dtos.FieldDTO;

import java.util.List;

public class CreateFields extends CommandBase<ProfileFields>
{
    public final List<FieldDTO> fieldDTOs;
    public final List<String> inMapFiles;

    public CreateFields(List<FieldDTO> fieldDTOs, List<String> inMapFiles)
    {
        this.fieldDTOs = fieldDTOs;
        this.inMapFiles = inMapFiles;
    }
}
