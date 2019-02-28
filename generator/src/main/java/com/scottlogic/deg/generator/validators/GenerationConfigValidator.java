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
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class used to determine whether the command line options are valid for generation.
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

        checkSwitches(config, generationConfigSource, errorMessages);

        checkProfileInputFile(errorMessages, generationConfigSource.getProfileFile());

        return new ValidationResult(errorMessages);
    }

    @Override
    public ValidationResult postProfileChecks(Profile profile,
                                              GenerationConfigSource configSource,
                                              OutputTarget outputTarget) throws IOException {
        ArrayList<String> errorMessages = new ArrayList<>();

        if (outputTarget instanceof FileOutputTarget) {
            if (configSource.shouldViolate()) {
                errorMessages.add(
                    checkViolationGenerateOutputTarget(configSource,
                        (FileOutputTarget) outputTarget,
                        profile.rules.size()
                    ));
            } else {
                errorMessages.add(
                    checkGenerateOutputTarget(configSource, (FileOutputTarget) outputTarget));
            }
        }
        errorMessages.removeAll(Arrays.asList("", null));
        return new ValidationResult(errorMessages);
    }

    private void checkSwitches(GenerationConfig config,
                               GenerationConfigSource configSource,
                               ArrayList<String> errorMessages) {

        if (config.getDataGenerationType() == GenerationConfig.DataGenerationType.RANDOM
            && !config.getMaxRows().isPresent()) {

            errorMessages.add("RANDOM mode requires max row limit: use -n=<row limit> option");
        }
        if (configSource.isEnableTracing()) {
            if (fileUtils.getTraceFile(configSource).exists() && !configSource.overwriteOutputFiles()) {
                errorMessages.add("Invalid Output - trace file already exists, please use a different output filename or use the --overwrite option");
            }
        }
    }

    private void checkProfileInputFile(ArrayList<String> errorMessages, File profileFile) {
        if (fileUtils.containsInvalidChars(profileFile)) {
            errorMessages.add(String.format("Profile file path (%s) contains one or more invalid characters ? : %% \" | > < ", profileFile.toString()));
        } else if (!profileFile.exists()) {
            errorMessages.add("Invalid Input - Profile file does not exist");
        } else if (profileFile.isDirectory()) {
            errorMessages.add("Invalid Input - Profile file path provided is to a directory");
        } else if (fileUtils.isFileEmpty(profileFile)) {
            errorMessages.add("Invalid Input - Profile file has no content");
        }
    }

    /**
     * Check the output target for a non-violating data generation.
     *
     * @param configSource
     * @param outputTarget
     */
    private String checkGenerateOutputTarget(GenerationConfigSource configSource,
                                             FileOutputTarget outputTarget) throws IOException {
        if (fileUtils.isDirectory(outputTarget)) {
            return
                "Invalid Output - target is a directory, please use a different output filename";
        } else if (!configSource.overwriteOutputFiles() && fileUtils.exists(outputTarget)) {
            return
                "Invalid Output - file already exists, please use a different output filename or use the --overwrite option";
        }

        if (!fileUtils.createDirectories(outputTarget.getFilePath().getParent())){
            return
                "Invalid Output - parent directory of output file already exists but is not a directory, please use a different output filename";
        }
        return "";
    }

    private String checkViolationGenerateOutputTarget(GenerationConfigSource configSource,
                                                      FileOutputTarget outputTarget,
                                                      int ruleCount) {
        if (!fileUtils.exists(outputTarget)) {
            return
                "Invalid Output - output directory must exist, please enter a valid directory name";
        } else if (!fileUtils.isDirectory(outputTarget)) {
            return
                "Invalid Output - not a directory, please enter a valid directory name";
        } else if (!configSource.overwriteOutputFiles() && !fileUtils
            .isDirectoryEmpty(outputTarget, ruleCount)) {
            return
                "Invalid Output - directory not empty, please remove any 'manifest.json' and '[0-9].csv' files or use the --overwrite option";
        }
        return "";
    }
}
