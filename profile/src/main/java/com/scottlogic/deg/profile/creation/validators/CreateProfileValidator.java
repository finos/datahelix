package com.scottlogic.deg.profile.creation.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.creation.commands.CreateProfile;
import com.scottlogic.deg.profile.creation.dtos.ProfileDTO;

public class CreateProfileValidator implements Validator<CreateProfile>
{
    private final Validator<ProfileDTO> profileValidator;

    @Inject
    public CreateProfileValidator(Validator<ProfileDTO> profileValidator)
    {
        this.profileValidator = profileValidator;
    }

    @Override
    public ValidationResult validate(CreateProfile command)
    {
        return profileValidator.validate(command.profileDTO);
    }
}
