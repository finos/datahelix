package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.OutputFormatLookup;
import com.scottlogic.deg.generator.config.detail.OutputFormatOption;
import com.scottlogic.deg.generator.inputs.validation.TypingRequiredPerFieldValidator;
import com.scottlogic.deg.generator.utils.FileUtils;
import com.scottlogic.deg.profile.serialisation.ValidationResult;
import com.scottlogic.deg.profile.v0_1.ProfileSchemaValidator;

import java.io.File;
import java.nio.file.Path;

/** A set of methods for conveniently creating typical validation rules. Could be broken apart to hew closer to SRP. */
public class ValidationRuleFactory {
    private final FileUtils fileUtils;
    private final ProfileSchemaValidator schemaBasedProfileValidator;
    private final TypingRequiredPerFieldValidator typednessValidator;
    private final OutputFormatLookup outputFormatLookup;

    @Inject
    ValidationRuleFactory(
        FileUtils fileUtils,
        ProfileSchemaValidator schemaBasedProfileValidator,
        TypingRequiredPerFieldValidator typednessValidator,
        OutputFormatLookup outputFormatLookup) {

        this.fileUtils = fileUtils;
        this.schemaBasedProfileValidator = schemaBasedProfileValidator;
        this.typednessValidator = typednessValidator;
        this.outputFormatLookup = outputFormatLookup;
    }

    private ValidationRule createRule(ConsumerBasedValidationRule.Implementation ruleImplementation) {
        return new ConsumerBasedValidationRule(ruleImplementation);
    }

    public ValidationRule outputFormatIsKnown(OutputFormatOption outputFormat) {
        return createRule(results -> {
            if (outputFormatLookup.get(outputFormat) == null) {
                results.addInputError("Output format was not recognised");
            }
        });
    }

    public ValidationRule outputFileIsAbsent(Path outputPath) {
        return createRule(results -> {
            if (fileUtils.exists(outputPath)) {
                results.addInputError(
                    "file already exists (%s), please use a different output filename or use the --replace option",
                    outputPath.toString());
            }
        });
    }

    public ValidationRule profilePathHasNoInvalidCharacters(File profileFile) {
        return createRule(results -> {
            if (fileUtils.containsInvalidChars(profileFile)) {
                results.addInputError(
                    "Profile file path (%s) contains one or more invalid characters ? : %% \" | > < ",
                    profileFile.toString());
            }
        });
    }

    public ValidationRule profileExists(File profileFile) {
        return createRule(results -> {
            if (!fileUtils.exists(profileFile.toPath())) {
                results.addInputError("Profile file (%s) does not exist", profileFile.toString());
            }
        });
    }

    public ValidationRule profilePathPointsAtAFile(File profileFile) {
        return createRule(results -> {
            if (!fileUtils.isFile(profileFile.toPath())) {
                results.addInputError("Profile file path (%s) provided is to a directory", profileFile.toString());
            }
        });
    }

    public ValidationRule profileFileIsNotEmpty(File profileFile) {
        return createRule(results -> {
            if (fileUtils.isFileEmpty(profileFile)) {
                results.addInputError("Profile file (%s) has no content", profileFile.toString());
            }
        });
    }

    public ValidationRule profileMatchesJsonSchema(File profileFile) {
        return createRule(results -> {
            ValidationResult schemaValidationResult = schemaBasedProfileValidator.validateProfile(profileFile);

            if (!schemaValidationResult.isValid()) {
                for (String errorMessage : schemaValidationResult.errorMessages) {
                    results.addInputError(errorMessage);
                }
            }
        });
    }

    public ValidationRule profileAssignsTypesToAllFields(Profile profile) {
        return () -> typednessValidator.validate(profile);
    }

    public ValidationRule outputPathIsNotADirectory(Path outputPath) {
        return createRule(results -> {
            if (fileUtils.isDirectory(outputPath)) {
                results.addInputError("target is a directory, please use a different output filename");
            }
        });
    }

    public ValidationRule outputPathAncestryExistsOrCanBeCreated(Path outputPath) {
        return createRule(results -> {
            if (fileUtils.exists(outputPath)) return;

            Path parent = outputPath.toAbsolutePath().getParent();

            // TODO: Move directory creation to point where we open a writer. Validators should have minimal side effects
            final boolean successfullyCreatedDirectories;
            try {
                successfullyCreatedDirectories = fileUtils.createDirectories(parent);
            } catch (Exception e) {
                results.addInputError("Couldn't set up parent directories of target path");
                return;
            }

            if (!successfullyCreatedDirectories) {
                results.addInputError(
                    "parent directory of output file already exists but is not a directory, please use a different output filename");
            }
        });
    }

    public ValidationRule outputDirectoryExistsOrCanBeCreated(Path outputPath) {
        return createRule(results -> {
            try {
                // TODO: Move this to the point where we open a writer. Validators should have minimal side effects
                fileUtils.createDirectories(outputPath);
            } catch (Exception e) {
                results.addInputError("Couldn't set up parent directories of target path");
            }
        });
    }

    public ValidationRule outputPathIsNotAFile(Path outputPath) {
        return createRule(results -> {
            if (fileUtils.exists(outputPath) && !fileUtils.isDirectory(outputPath)) {
                results.addInputError("not a directory, please enter a valid directory name");
            }
        });
    }

    public ValidationRule outputDirectoryHasRoomForViolationFiles(Path outputPath, int numberOfRulesInProfile) {
        return createRule(results -> {
            if (fileUtils.exists(outputPath) && !fileUtils.isDirectoryEmpty(outputPath, numberOfRulesInProfile)) {
                results.addInputError(
                    "directory not empty, please remove any 'manifest.json' and '[0-9].csv' files or use the --replace option");
            }
        });
    }
}
