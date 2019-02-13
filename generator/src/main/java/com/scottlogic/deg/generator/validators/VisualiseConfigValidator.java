package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.visualise.VisualiseConfigSource;

import java.util.ArrayList;

/**
 * Class used to determine whether the command line options are valid for visualisation
 */
public class VisualiseConfigValidator {

    private final OutputTarget outputTarget;
    private final VisualiseConfigSource configSource;

    @Inject
    public VisualiseConfigValidator(VisualiseConfigSource configSource,
                                        OutputTarget outputTarget) {
        this.configSource = configSource;
        this.outputTarget = outputTarget;
    }

    public ValidationResult validateCommandLine() {
        ArrayList<String> errorMessages = new ArrayList<>();
        ValidationResult validationResult = new ValidationResult(errorMessages);

        if (!configSource.overwriteOutputFiles()) {
                checkOutputTarget(errorMessages, outputTarget);
        }

        return validationResult;
    }

    private void checkOutputTarget(ArrayList<String> errorMessages, OutputTarget outputTarget) {
        if (outputTarget.isDirectory()) {
            errorMessages.add("Invalid Output - target is a directory, please use a different output filename or use the --overwrite option");
        } else if (outputTarget.exists()) {
            errorMessages.add("Invalid Output - file already exists, please use a different output filename or use the --overwrite option");
        }
    }

}