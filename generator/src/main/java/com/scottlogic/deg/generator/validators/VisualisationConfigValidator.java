package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.utils.FileUtils;

import java.util.ArrayList;

/**
 * Class used to determine whether the command line options are valid for visualisation
 */
public class VisualisationConfigValidator {

    private final FileUtils fileUtils;

    @Inject
    public VisualisationConfigValidator(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    /**
     * @return the result of command line validation. Contains a list of error messages.
     * if the list is empty then the validation was successful.
     */
    public ValidationResult validateCommandLine(boolean overwrite, OutputTarget outputTarget) {
        return new ValidationResult(checkOutputTarget(overwrite, outputTarget));
    }

    /**
     * make sure the output file specified on the command line is valid. to be a valid output target file
     * the target must not be a directory, and must not already exist.
     *
     * @param outputTarget the output target to check for validity.
     * @return the list of error messages to append to if the output target is not valid.
     */
    private ArrayList<String> checkOutputTarget(boolean overwrite, OutputTarget outputTarget) {
        ArrayList<String> errorMessages = new ArrayList<>();
        if (outputTarget instanceof FileOutputTarget) {
            if (fileUtils.isDirectory((FileOutputTarget) outputTarget)) {
                errorMessages.add(
                    "Invalid Output - target is a directory, please use a different output filename");
            } else if (!overwrite && fileUtils.exists((FileOutputTarget) outputTarget)) {
                errorMessages.add(
                    "Invalid Output - file already exists, please use a different output filename or use the --overwrite option");
            }
        }
        return errorMessages;
    }

}