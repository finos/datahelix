package com.scottlogic.deg.orchestrator.validator;

import com.google.inject.Inject;
import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.orchestrator.guice.AllConfigSource;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.outputs.targets.OutputTargetValidationException;
import com.scottlogic.deg.generator.utils.FileUtils;

import java.io.File;

/**
 * Class used to determine whether the command line options are valid for generation.
 */
public class ConfigValidator {

    private final FileUtils fileUtils;

    @Inject
    public ConfigValidator(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    public void preProfileChecks(AllConfigSource generationConfigSource) {
        checkSwitches(generationConfigSource);

        checkProfileInputFile(generationConfigSource.getProfileFile());

    }

    private void checkSwitches(GenerationConfigSource configSource) {

        if (configSource.isEnableTracing() && fileUtils.getTraceFile(configSource.getOutputPath()).exists() && !configSource.overwriteOutputFiles()) {
                throw new OutputTargetValidationException("trace file already exists, please use a different output filename or use the --replace option");
            }

    }

    private void checkProfileInputFile(File profileFile) {
        if (fileUtils.containsInvalidChars(profileFile)) {
            throw new ValidationException("Profile file path " + profileFile + " contains one or more invalid characters ? : %% \" | > < ");
        }
        else if (!profileFile.exists()) {
            throw new ValidationException("Profile file " + profileFile + " does not exist");
        }
        else if (profileFile.isDirectory()) {
            throw new ValidationException("Profile file path " + profileFile + " provided is to a directory");
        }
        else if (fileUtils.isFileEmpty(profileFile)) {
            throw new ValidationException("Profile file " + profileFile + " has no content");
        }
    }
}

