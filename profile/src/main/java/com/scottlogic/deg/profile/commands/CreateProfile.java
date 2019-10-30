package com.scottlogic.deg.profile.commands;

import com.scottlogic.deg.common.commands.CommandBase;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.profile.dtos.ProfileDTO;

public class CreateProfile extends CommandBase<Profile>
{
    public final ProfileDTO dto;

    public CreateProfile(ProfileDTO dto)
    {
        this.dto = dto;
    }
}
