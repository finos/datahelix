package com.scottlogic.deg.generator.cucumber.utils;

import com.scottlogic.deg.schemas.common.ValidationResult;
import com.scottlogic.deg.schemas.v0_1.ProfileSchemaValidator;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class CucumberProfileSchemaValidator implements ProfileSchemaValidator {
    @Override
    public ValidationResult validateProfile(File profileFile) {
        return new ValidationResult(new ArrayList<>());
    }

    @Override
    public ValidationResult validateProfile(InputStream profileStream) {
        return new ValidationResult(new ArrayList<>());
    }

    @Override
    public ValidationResult validateProfile(InputStream schemaStream, InputStream profileStream) {
        return new ValidationResult(new ArrayList<>());
    }
}
