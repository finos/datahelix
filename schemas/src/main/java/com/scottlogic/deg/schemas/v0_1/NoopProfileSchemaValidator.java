package com.scottlogic.deg.schemas.v0_1;

import com.scottlogic.deg.schemas.common.ValidationResult;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;

public class NoopProfileSchemaValidator implements ProfileSchemaValidator {

    ValidationResult result = new ValidationResult(Collections.emptyList());

    @Override
    public ValidationResult validateProfile(File profileFile) {
        return result;
    }
}
