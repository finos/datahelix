package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.utils.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * Class used to determine whether the command line options are valid for generation
 */
public class GenerationConfigValidator implements ConfigValidator {

    private final FileUtils fileUtils;
    private final GenerationConfigSource configSource;
    private final OutputTarget outputTarget;

    @Inject
    public GenerationConfigValidator(FileUtils fileUtils,
                                     GenerationConfigSource configSource,
                                     OutputTarget outputTarget) {
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

        checkProfileInputFile(errorMessages);

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

    private void checkProfileInputFile(ArrayList<String> errorMessages) {
        if (!configSource.getProfileFile().exists()){
            errorMessages.add("Invalid Input - Profile file does not exist");

            if (configSource.getProfileFile().toString().matches(".*[?%*|><\"].*|^(?:[^:]*+:){2,}[^:]*+$")) {
                errorMessages.add(String.format("Profile file path (%s) contains one or more invalid characters ? : %% \" | > < ", configSource.getProfileFile().toString()));
            }
        }
        else if (configSource.getProfileFile().isDirectory()) {
            errorMessages.add("Invalid Input - Profile file path provided is to a directory");
        }
        else if (configSource.getProfileFile().length() == 0) {
            errorMessages.add("Invalid Input - Profile file has no content");
        }
        else {
            try {
                String fileType = Files.probeContentType(configSource.getProfileFile().toPath());
                if (!fileType.equals("application/json")) {
                    errorMessages.add(String.format("Invalid Input - File is of type %s\nFile type application/json required", fileType));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkGenerateOutputTarget(ArrayList<String> errorMessages,
                                           OutputTarget outputTarget) {
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

    private void checkViolationGenerateOutputTarget(ArrayList<String> errorMessages,
                                                    OutputTarget outputTarget, int ruleCount) {
        if (outputTarget instanceof FileOutputTarget) {
            if (!fileUtils.exists((FileOutputTarget) outputTarget)) {
                errorMessages.add(
                    "Invalid Output - output directory must exist, please enter a valid directory name");
            } else if (!fileUtils.isDirectory((FileOutputTarget) outputTarget)) {
                errorMessages
                    .add("Invalid Output - not a directory, please enter a valid directory name");
            } else if (!configSource.overwriteOutputFiles() && !fileUtils
                .isDirectoryEmpty((FileOutputTarget) outputTarget, ruleCount)) {
                errorMessages.add(
                    "Invalid Output - directory not empty, please remove any 'manifest.json' and '[0-9].csv' files or use the --overwrite option");
            }
        }
    }

}
