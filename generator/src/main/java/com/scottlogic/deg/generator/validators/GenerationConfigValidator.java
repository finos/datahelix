package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.utils.FileUtils;

import java.util.ArrayList;

/**
 * Class used to determine whether the command line options are valid for generation
 */
public class GenerationConfigValidator {

    private final OutputTarget outputTarget;
    private final GenerationConfigSource configSource;
    private final FileUtils fileUtils;

    @Inject
    public GenerationConfigValidator(GenerationConfigSource configSource,
                                     OutputTarget outputTarget,
                                     FileUtils fileUtils) {
        this.configSource = configSource;
        this.outputTarget = outputTarget;
        this.fileUtils = fileUtils;
    }

    public ValidationResult validateCommandLinePreProfile(GenerationConfig config) {
        ArrayList<String> errorMessages = new ArrayList<>();
        ValidationResult validationResult = new ValidationResult(errorMessages);

        if (config.getDataGenerationType() == GenerationConfig.DataGenerationType.RANDOM
            && !config.getMaxRows().isPresent()) {

            errorMessages.add("RANDOM mode requires max row limit: use -n=<row limit> option");
        }

        return validationResult;
    }

    public ValidationResult validateCommandLinePostProfile(Profile profile) {
        ArrayList<String> errorMessages = new ArrayList<>();
        ValidationResult validationResult = new ValidationResult(errorMessages);

        if (configSource.shouldViolate()) {
            checkViolationGenerateOutputTarget(errorMessages, outputTarget, profile.rules.size());
        } else {
            checkGenerateOutputTarget(errorMessages, outputTarget);
        }

        return validationResult;
    }

    private void checkGenerateOutputTarget(ArrayList<String> errorMessages,
                                           OutputTarget outputTarget) {
        if (outputTarget instanceof FileOutputTarget) {
            if (fileUtils.isDirectory((FileOutputTarget)outputTarget)) {
                errorMessages.add(
                    "Invalid Output - target is a directory, please use a different output filename");
            } else if (!configSource.overwriteOutputFiles() && fileUtils.exists((FileOutputTarget)outputTarget)) {
                errorMessages.add(
                    "Invalid Output - file already exists, please use a different output filename or use the --overwrite option");
            }
        }
    }

    private void checkViolationGenerateOutputTarget(ArrayList<String> errorMessages,
                                                    OutputTarget outputTarget, int ruleCount) {
        if (outputTarget instanceof FileOutputTarget) {
            if (!fileUtils.exists((FileOutputTarget)outputTarget)) {
                errorMessages.add(
                    "Invalid Output - output directory must exist. please enter a valid directory name");
            } else if (!fileUtils.isDirectory((FileOutputTarget)outputTarget)) {
                errorMessages
                    .add("Invalid Output - not a directory. please enter a valid directory name");
            } else if (!configSource.overwriteOutputFiles() && !fileUtils
                .isDirectoryEmpty((FileOutputTarget)outputTarget, ruleCount)) {
                errorMessages.add(
                    "Invalid Output - directory not empty. please remove any 'manfiest.json' and '[0-9].csv' files or use the --overwrite option");
            }
        }
    }

}
