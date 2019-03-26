package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.ValidationType;
import com.scottlogic.deg.generator.inputs.validation.messages.OutputValidationMessage;
import com.scottlogic.deg.generator.inputs.validation.messages.StandardValidationMessages;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.utils.FileUtils;
import com.scottlogic.deg.schemas.common.ValidationResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Class used to determine whether the command line options are valid for generation.
 */
public class GenerationConfigValidator implements ConfigValidator {

    private final FileUtils fileUtils;

    @Inject
    public GenerationConfigValidator(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    @Override
    public ValidationResult preProfileChecks(GenerationConfig config, GenerationConfigSource generationConfigSource) {
        ArrayList<String> errorMessages = new ArrayList<>();

        checkSwitches(generationConfigSource, errorMessages);

        checkProfileInputFile(errorMessages, generationConfigSource.getProfileFile());

        return new ValidationResult(errorMessages);
    }

    private void checkSwitches(GenerationConfigSource configSource,
                               ArrayList<String> errorMessages) {

        if (configSource.isEnableTracing()) {
            if (fileUtils.getTraceFile(configSource).exists() && !configSource.overwriteOutputFiles()) {
                errorMessages.add("Invalid Output - trace file already exists, please use a different output filename or use the --replace option");
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
}

