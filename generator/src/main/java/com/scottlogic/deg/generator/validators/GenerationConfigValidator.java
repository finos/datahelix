package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.ValidationType;
import com.scottlogic.deg.generator.inputs.validation.messages.InputValidationMessage;
import com.scottlogic.deg.generator.inputs.validation.messages.OutputValidationMessage;
import com.scottlogic.deg.generator.utils.FileUtils;

import java.io.File;
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
    public Collection<ValidationAlert> preProfileChecks(GenerationConfigSource generationConfigSource) {
        ArrayList<ValidationAlert> errorMessages = new ArrayList<>();

        checkSwitches(generationConfigSource, errorMessages);

        checkProfileInputFile(errorMessages, generationConfigSource.getProfileFile());

        return errorMessages;
    }

    private void checkSwitches(GenerationConfigSource configSource,
                               ArrayList<ValidationAlert> errorMessages) {

        if (configSource.isEnableTracing()) {
            if (fileUtils.getTraceFile(configSource).exists() && !configSource.overwriteOutputFiles()) {
                errorMessages.add(new ValidationAlert(
                    Criticality.ERROR,
                    new OutputValidationMessage("trace file already exists, please use a different output filename or use the --replace option"),
                    ValidationType.OUTPUT,
                    null));
            }
        }
    }

    private void checkProfileInputFile(ArrayList<ValidationAlert> errorMessages, File profileFile) {
        if (fileUtils.containsInvalidChars(profileFile)) {
            errorMessages.add(
                new ValidationAlert(
                    Criticality.ERROR,
                    new InputValidationMessage("Profile file path (%s) contains one or more invalid characters ? : %% \" | > < ", profileFile),
                    ValidationType.INPUT,
                    null));
        } else if (!profileFile.exists()) {
            errorMessages.add(
                new ValidationAlert(
                    Criticality.ERROR,
                    new InputValidationMessage("Profile file (%s) does not exist", profileFile),
                    ValidationType.INPUT,
                    null));
        } else if (profileFile.isDirectory()) {
            errorMessages.add(
                new ValidationAlert(
                    Criticality.ERROR,
                    new InputValidationMessage("Profile file path (%s) provided is to a directory", profileFile),
                    ValidationType.INPUT,
                    null));
        } else if (fileUtils.isFileEmpty(profileFile)) {
            errorMessages.add(
                new ValidationAlert(
                    Criticality.ERROR,
                    new InputValidationMessage("Profile file (%s) has no content", profileFile),
                    ValidationType.INPUT,
                    null));
        }
    }
}

