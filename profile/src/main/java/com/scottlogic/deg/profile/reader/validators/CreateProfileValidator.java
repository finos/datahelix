package com.scottlogic.deg.profile.reader.validators;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.reader.commands.CreateProfile;

public class CreateProfileValidator implements Validator<CreateProfile>
{
    @Override
    public ValidationResult validate(CreateProfile createProfile)
    {
        //TODO
        return ValidationResult.success();
    }
}
