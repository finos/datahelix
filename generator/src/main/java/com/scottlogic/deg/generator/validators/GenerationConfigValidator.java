package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 * Class used to determine whether the command line options are valid for generation
 */
public class GenerationConfigValidator implements ConfigValidator {

    private final FileUtils fileUtils;

    @Inject
    public GenerationConfigValidator(FileUtils fileUtils,
                                     GenerationConfigSource configSource,
                                     OutputTarget outputTarget) {
        this.fileUtils = fileUtils;
    }

    @Override
    public ValidationResult preProfileChecks(GenerationConfig config, GenerationConfigSource generationConfigSource) {
        ArrayList<String> errorMessages = new ArrayList<>();

        checkSwitches(config, errorMessages);

        checkProfileInputFile(errorMessages, generationConfigSource.getProfileFile());

        return new ValidationResult(errorMessages);
    }

    @Override
    public ValidationResult postProfileChecks(Profile profile,
                                              GenerationConfigSource configSource,
                                              OutputTarget outputTarget) {
        ArrayList<String> errorMessages = new ArrayList<>();
        ValidationResult validationResult = new ValidationResult(errorMessages);

        if (outputTarget instanceof FileOutputTarget ) {
            if (configSource.shouldViolate()) {
                checkViolationGenerateOutputTarget(
                    errorMessages,
                    configSource,
                    (FileOutputTarget)outputTarget,
                    profile.rules.size()
                );
            } else {
                checkGenerateOutputTarget(errorMessages, configSource, (FileOutputTarget)outputTarget);
            }
        }

        return validationResult;
    }

    private void checkSwitches (GenerationConfig config, ArrayList<String> errorMessages) {
        if (config.getDataGenerationType() == GenerationConfig.DataGenerationType.RANDOM
            && !config.getMaxRows().isPresent()) {

            errorMessages.add("RANDOM mode requires max row limit: use -n=<row limit> option");
        }
    }

    private void checkProfileInputFile(ArrayList<String> errorMessages, File profileFile) {
        if (fileUtils.containsInvalidChars(profileFile)) {
            errorMessages.add(String.format("Profile file path (%s) contains one or more invalid characters ? : %% \" | > < ", profileFile.toString()));
        }
        else if (!profileFile.exists()){
            errorMessages.add("Invalid Input - Profile file does not exist");
        }
        else if (profileFile.isDirectory()) {
            errorMessages.add("Invalid Input - Profile file path provided is to a directory");
        }
        else if (fileUtils.isFileEmpty(profileFile)) {
            errorMessages.add("Invalid Input - Profile file has no content");
        }
        else {
            try {
                String fileType = fileUtils.probeContentType(profileFile.toPath());
                if (!fileType.equals("application/json")) {
                    errorMessages.add(String.format("Invalid Input - File is of type %s\nFile type application/json required", fileType));
                }
            } catch (IOException e) {
                e.printStackTrace();
                errorMessages.add("Invalid Input - Unable to determine content type of profile file");
            }
        }
    }

    private void checkGenerateOutputTarget(ArrayList<String> errorMessages,
                                           GenerationConfigSource configSource,
                                           FileOutputTarget outputTarget) {
        if (fileUtils.isDirectory(outputTarget)) {
            errorMessages.add(
                "Invalid Output - target is a directory, please use a different output filename");
        } else if (!configSource.overwriteOutputFiles() && fileUtils.exists(outputTarget)) {
            errorMessages.add(
                "Invalid Output - file already exists, please use a different output filename or use the --overwrite option");
        }
    }

    private void checkViolationGenerateOutputTarget(ArrayList<String> errorMessages,
                                                    GenerationConfigSource configSource,
                                                    FileOutputTarget outputTarget,
                                                    int ruleCount) {
        if (!fileUtils.exists(outputTarget)) {
            errorMessages.add(
                "Invalid Output - output directory must exist, please enter a valid directory name");
        } else if (!fileUtils.isDirectory(outputTarget)) {
            errorMessages
                .add("Invalid Output - not a directory, please enter a valid directory name");
        } else if (!configSource.overwriteOutputFiles() && !fileUtils
            .isDirectoryEmpty(outputTarget, ruleCount)) {
            errorMessages.add(
                    "Invalid Output - directory not empty, please remove any 'manifest.json' and '[0-9].csv' files or use the --overwrite option");
        }
    }
}
