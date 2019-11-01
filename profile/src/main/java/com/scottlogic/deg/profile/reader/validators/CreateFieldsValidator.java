package com.scottlogic.deg.profile.reader.validators;

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.reader.commands.CreateFields;

public class CreateFieldsValidator implements Validator<CreateFields>
{
    @Override
    public ValidationResult validate(CreateFields createFields)
    {
        //TODO
        return ValidationResult.success();
    }
}
