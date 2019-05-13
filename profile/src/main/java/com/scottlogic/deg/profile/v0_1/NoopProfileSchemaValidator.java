package com.scottlogic.deg.profile.v0_1;

import com.scottlogic.deg.profile.common.ValidationResult;

import java.io.File;
import java.util.Collections;

public class NoopProfileSchemaValidator implements ProfileSchemaValidator {

    ValidationResult result = new ValidationResult(Collections.emptyList());

    @Override
    public ValidationResult validateProfile(File profileFile) {
        return result;
    }
}
