package com.scottlogic.deg.profile.commands;

import com.scottlogic.deg.common.commands.CommandBase;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.profile.dtos.FieldDTO;

public class CreateField extends CommandBase<Field>
{
    public final FieldDTO dto;

    public CreateField(FieldDTO dto)
    {
        this.dto = dto;
    }
}
