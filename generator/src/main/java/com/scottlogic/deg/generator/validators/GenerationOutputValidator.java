package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.ValidationType;
import com.scottlogic.deg.generator.inputs.validation.messages.OutputValidationMessage;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

public class GenerationOutputValidator implements ProfileValidator {
    private final FileUtils fileUtils;
    private final GenerationConfigSource configSource;
    private final OutputTarget outputTarget;

    @Inject
    public GenerationOutputValidator(FileUtils fileUtils, GenerationConfigSource configSource, OutputTarget outputTarget) {
        this.fileUtils = fileUtils;
        this.configSource = configSource;
        this.outputTarget = outputTarget;
    }

    @Override
    public Collection<ValidationAlert> validate(Profile profile) {
        ArrayList<ValidationAlert> errorMessages = new ArrayList<>();

        try {
            if (outputTarget instanceof FileOutputTarget) {
                if (configSource.shouldViolate()) {
                    checkViolationGenerateOutputTarget(
                        errorMessages,
                        configSource,
                        (FileOutputTarget) outputTarget,
                        profile.rules.size()
                    );
                } else {
                    checkGenerateOutputTarget(errorMessages, configSource, (FileOutputTarget) outputTarget);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return errorMessages;
    }

    /**
     * Check the output target for a non-violating data generation.
     *
     * @param errorMessages the list of error messages that we will add to
     * @param configSource  used to determine whether the user has opted to automatically overwrite output files
     * @param outputTarget  the output file that the user selected
     */
    private void checkGenerateOutputTarget(ArrayList<ValidationAlert> errorMessages,
                                           GenerationConfigSource configSource,
                                           FileOutputTarget outputTarget) throws IOException {
        if (fileUtils.isDirectory(outputTarget.getFilePath())) {
            errorMessages.add(
                new ValidationAlert(
                    Criticality.ERROR,
                    new OutputValidationMessage("target is a directory, please use a different output filename"),
                    ValidationType.OUTPUT,
                    null));
        } else if (!configSource.overwriteOutputFiles() && fileUtils.exists(outputTarget.getFilePath())) {
            errorMessages.add(
                new ValidationAlert(
                    Criticality.ERROR,
                    new OutputValidationMessage("file already exists, please use a different output filename or use the --replace option"),
                    ValidationType.OUTPUT,
                    null));
        } else if (!fileUtils.exists(outputTarget.getFilePath())) {
            Path parent = outputTarget.getFilePath().toAbsolutePath().getParent();
            if (!fileUtils.createDirectories(parent)) {
                errorMessages.add(
                    new ValidationAlert(
                        Criticality.ERROR,
                        new OutputValidationMessage("parent directory of output file already exists but is not a directory, please use a different output filename"),
                        ValidationType.OUTPUT,
                        null));
            }
        }
    }

    private void checkViolationGenerateOutputTarget(ArrayList<ValidationAlert> errorMessages,
                                                    GenerationConfigSource configSource,
                                                    FileOutputTarget outputTarget,
                                                    int ruleCount) throws IOException {
        if (!fileUtils.exists(outputTarget.getFilePath())) {
            fileUtils.createDirectories(outputTarget.getFilePath());
        } else if (!fileUtils.isDirectory(outputTarget.getFilePath())) {
            errorMessages
                .add(new ValidationAlert(
                    Criticality.ERROR,
                    new OutputValidationMessage("not a directory, please enter a valid directory name"),
                    ValidationType.OUTPUT,
                    null));
        } else if (!configSource.overwriteOutputFiles() && !fileUtils
            .isDirectoryEmpty(outputTarget.getFilePath(), ruleCount)) {
            errorMessages.add(
                new ValidationAlert(
                    Criticality.ERROR,
                    new OutputValidationMessage("directory not empty, please remove any 'manifest.json' and '[0-9].csv' files or use the --replace option"),
                    ValidationType.OUTPUT,
                    null));
        }
    }
}
