package com.scottlogic.deg.profile.reader.validators;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.reader.commands.CreateConstraints;

public class CreateConstraintsValidator implements Validator<CreateConstraints>
{
    @Override
    public ValidationResult validate(CreateConstraints createConstraints)
    {
        return ValidationResult.success();
    }
}
