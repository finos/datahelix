package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.ArrayList;

/**
 * Class used to determine whether the command line options are valid for generation
 * */
public class GenerationConfigValidator {

    public ValidationResult validateCommandLine(GenerationConfig config) {
        ArrayList<String> errorMessages = new ArrayList<>();
        ValidationResult validationResult = new ValidationResult(errorMessages);

        if (config.getDataGenerationType() == GenerationConfig.DataGenerationType.RANDOM
            && !config.getMaxRows().isPresent()) {

            errorMessages.add("RANDOM mode requires max row limit: use -n=<row limit> option");
        }

        return validationResult;
    }
}