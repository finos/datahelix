package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.utils.FileUtils;
import com.scottlogic.deg.generator.visualisation.VisualisationConfigSource;

import java.util.ArrayList;

/**
 * Class used to determine whether the command line options are valid for visualisation
 */
public class VisualisationConfigValidator {

    private final FileUtils fileUtils;
    private final OutputTarget outputTarget;
    private final VisualisationConfigSource configSource;

    @Inject
    public VisualisationConfigValidator(FileUtils fileUtils,
                                        OutputTarget outputTarget,
                                        VisualisationConfigSource configSource) {
        this.configSource = configSource;
        this.outputTarget = outputTarget;
        this.fileUtils = fileUtils;
    }

    /**
     * @return the result of command line validation. Contains a list of error messages.
     *         if the list is empty then the validation was successful.
     */
    public ValidationResult validateCommandLine() {
        ArrayList<String> errorMessages = new ArrayList<>();
        ValidationResult validationResult = new ValidationResult(errorMessages);

        checkOutputTarget(errorMessages, outputTarget);

        return validationResult;
    }

    /**
     * make sure the output file specified on the command line is valid.
     *
     * @param errorMessages the list of error messages to append to if the output target is not valid.
     * @param outputTarget  the output target to check for validity.
     */
    private void checkOutputTarget(ArrayList<String> errorMessages, OutputTarget outputTarget) {
        if (outputTarget instanceof FileOutputTarget) {
            if (fileUtils.isDirectory((FileOutputTarget) outputTarget)) {
                errorMessages.add(
                    "Invalid Output - target is a directory, please use a different output filename");
            } else if (!configSource.overwriteOutputFiles() && fileUtils.exists((FileOutputTarget) outputTarget)) {
                errorMessages.add(
                    "Invalid Output - file already exists, please use a different output filename or use the --overwrite option");
            }
        }
    }

}