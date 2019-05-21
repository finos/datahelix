package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.utils.FileUtils;
import com.scottlogic.deg.profile.serialisation.ValidationResult;

import java.nio.file.Path;
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
    public ValidationResult validateCommandLine(boolean overwrite, Path outputPath) {
        return new ValidationResult(checkOutputTarget(overwrite, outputPath));
    }

    /**
     * make sure the output file specified on the command line is valid. to be a valid output target file
     * the target must not be a directory, and must not already exist.
     *
     * @param outputPath the output target to check for validity.
     * @return the list of error messages to append to if the output target is not valid.
     */
    private ArrayList<String> checkOutputTarget(boolean overwrite, Path outputPath) {
        ArrayList<String> errorMessages = new ArrayList<>();
        if (fileUtils.isDirectory(outputPath)) {
            errorMessages.add(
                "Invalid Output - target is a directory, please use a different output filename");
        } else if (!overwrite && fileUtils.exists(outputPath)) {
            errorMessages.add(
                "Invalid Output - file already exists, please use a different output filename or use the --overwrite option");
        }

        return errorMessages;
    }

}
