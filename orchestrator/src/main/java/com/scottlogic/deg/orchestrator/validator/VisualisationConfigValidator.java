package com.scottlogic.deg.orchestrator.validator;

import com.google.inject.Inject;
import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.generator.utils.FileUtils;

import java.nio.file.Path;

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
    public void validateCommandLine(boolean overwrite, Path outputPath) {
        if (fileUtils.isDirectory(outputPath)) {
            throw new ValidationException(
                "Invalid Output - target is a directory, please use a different output filename"
            );
        }
         if (!overwrite && fileUtils.exists(outputPath)) {
            throw new ValidationException(
                "Invalid Output - file already exists, please use a different output filename " +
                    "or use the --overwrite option"
            );
        }
    }
}
