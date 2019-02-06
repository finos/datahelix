package com.scottlogic.deg.generator.CommandLine;

import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.ArrayList;

/**
 * Class used to determine whether the command line options are valid for generation
 * */
public class CommandLineValidator {

    public ValidationResult validateCommandLine(GenerationConfig config) {

        ArrayList<String> errorMessages = new ArrayList<>();
        ValidationResult validationResult = new ValidationResult(errorMessages);

        if (config.getDataGenerationType() == GenerationConfig.DataGenerationType.RANDOM
            && !config.getMaxRows().isPresent()) {

            errorMessages.add("RANDOM mode requires max row limit\nuse -n=<row limit> option\n");
        }

        return validationResult;
    }

}







