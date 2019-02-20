package com.scottlogic.deg.generator.cucumber.utils;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.validators.ConfigValidator;
import com.scottlogic.deg.generator.validators.ValidationResult;

import java.util.ArrayList;

public class CucumberGenerationConfigValidator implements ConfigValidator {

    @Override
    public ValidationResult validatePreProfile(GenerationConfig config) {
        return new ValidationResult(new ArrayList<>());
    }

    @Override
    public ValidationResult validateCommandLinePostProfile(Profile profile) {
        return new ValidationResult(new ArrayList<>());
    }


}
