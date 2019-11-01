package com.scottlogic.deg.profile.creation.commands;

import com.scottlogic.deg.common.commands.Command;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.profile.creation.dtos.ProfileDTO;

public class CreateProfile extends Command<Profile>
{
    public final ProfileDTO profileDTO;

    public CreateProfile(ProfileDTO profileDTO)
    {
        this.profileDTO = profileDTO;
    }
}
