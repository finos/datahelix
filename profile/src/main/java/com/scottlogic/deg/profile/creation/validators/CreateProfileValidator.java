package com.scottlogic.deg.profile.creation.validators;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.creation.commands.CreateProfile;

public class CreateProfileValidator implements Validator<CreateProfile>
{
    @Override
    public ValidationResult validate(CreateProfile createProfile)
    {
        //TODO
        return ValidationResult.success();
    }
}
