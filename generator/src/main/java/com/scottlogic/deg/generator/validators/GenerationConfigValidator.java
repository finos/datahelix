package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;

import java.util.ArrayList;

/**
 * Class used to determine whether the command line options are valid for generation
 */
public class GenerationConfigValidator {

    private final OutputTarget outputTarget;
    private final GenerationConfigSource configSource;

    @Inject
    public GenerationConfigValidator(GenerationConfigSource configSource,
                                     OutputTarget outputTarget) {
        this.configSource = configSource;
        this.outputTarget = outputTarget;
    }

    public ValidationResult validateCommandLine(GenerationConfig config) {
        ArrayList<String> errorMessages = new ArrayList<>();
        ValidationResult validationResult = new ValidationResult(errorMessages);

        if (config.getDataGenerationType() == GenerationConfig.DataGenerationType.RANDOM
            && !config.getMaxRows().isPresent()) {

            errorMessages.add("RANDOM mode requires max row limit: use -n=<row limit> option");
        }

        if (!configSource.overwriteOutputFiles()) {
            if (configSource.shouldViolate()) {
                checkViolationGenerateOutputTarget(errorMessages, outputTarget);
            } else {
                checkGenerateOutputTarget(errorMessages, outputTarget);
            }
        }

        return validationResult;
    }

    private void checkGenerateOutputTarget(ArrayList<String> errorMessages, OutputTarget outputTarget) {
        if (outputTarget.isDirectory()) {
            errorMessages.add("Invalid Output - target is a directory, please use a different output filename");
        } else if (outputTarget.exists()) {
            errorMessages.add("Invalid Output - file already exists, please use a different output filename or use the --overwrite option");
        }
    }

    private void checkViolationGenerateOutputTarget(ArrayList<String> errorMessages, OutputTarget outputTarget) {
        if (!outputTarget.isDirectory()) {
            errorMessages.add("Invalid Output - not a directory. please enter a valid directory name");
        }
    }

}