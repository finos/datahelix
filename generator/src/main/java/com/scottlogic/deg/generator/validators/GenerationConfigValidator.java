package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.outputs.targets.FileOutputTarget;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Class used to determine whether the command line options are valid for generation
 * */
public class GenerationConfigValidator {

    public ValidationResult validateCommandLine(GenerationConfig config, OutputTarget fileOutputTarget) {
        ArrayList<String> errorMessages = new ArrayList<>();
        ValidationResult validationResult = new ValidationResult(errorMessages);

        if (config.getDataGenerationType() == GenerationConfig.DataGenerationType.RANDOM
            && !config.getMaxRows().isPresent()) {

            errorMessages.add("RANDOM mode requires max row limit: use -n=<row limit> option");
        }

        if (outputFileAlreadyExists(fileOutputTarget)) {
            errorMessages.add("Output file already exists, please use a different output filename");
        }

        return validationResult;
    }

    private boolean outputFileAlreadyExists(OutputTarget fileOutputTarget) {
        if (fileOutputTarget instanceof FileOutputTarget) {
            Path path = (((FileOutputTarget) fileOutputTarget).getFilePath());
            return Files.exists(path);
        }
        return false;
    }
}