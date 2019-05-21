package com.scottlogic.deg.generator;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.outputs.formats.OutputFormat;
import com.scottlogic.deg.generator.outputs.formats.trace.TraceOutputFormat;
import com.scottlogic.deg.generator.outputs.targets.*;
import com.scottlogic.deg.generator.utils.FileUtils;
import com.scottlogic.deg.generator.utils.FileUtilsImpl;
import com.scottlogic.deg.generator.validators.ErrorReporter;
import com.scottlogic.deg.generator.validators.ValidationException;
import com.scottlogic.deg.generator.validators.ValidationRuleFactory;
import com.scottlogic.deg.generator.validators.Validator;
import com.scottlogic.deg.generator.violations.ViolationGenerationEngine;
import com.scottlogic.deg.profile.reader.ProfileReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class GenerateExecute implements Runnable {
    private final ErrorReporter errorReporter;
    private final GenerationConfigSource configSource;

    private final OutputFormatLookup outputFormatLookup;

    private final StandardGenerationEngine standardGenerationEngine;
    private final ViolationGenerationEngine violationGenerationEngine;

    private final ProfileReader profileReader;

    private final Validator validator;
    private final ValidationRuleFactory validationRules;

    private final FileUtils fileUtils;

    @Inject
    GenerateExecute(
        ErrorReporter errorReporter,
        GenerationConfigSource configSource,
        OutputFormatLookup outputFormatLookup,
        StandardGenerationEngine standardGenerationEngine,
        ViolationGenerationEngine violationGenerationEngine,
        ProfileReader profileReader,
        Validator validator,
        ValidationRuleFactory validationRules,
        FileUtils fileUtils) {

        this.errorReporter = errorReporter;
        this.configSource = configSource;
        this.outputFormatLookup = outputFormatLookup;
        this.standardGenerationEngine = standardGenerationEngine;
        this.violationGenerationEngine = violationGenerationEngine;
        this.profileReader = profileReader;
        this.validator = validator;
        this.validationRules = validationRules;
        this.fileUtils = fileUtils;
    }

    public void run() {
        try {
            runInner();
        } catch (ValidationException e) {
            // validation errors are outputted to the user by the validator object, so nothing left to do here
        } catch (Exception e) {
            errorReporter.displayException(e);
        }
    }

    /** Does all the main work, sans exception handling */
    private void runInner() throws IOException {
        validator.validateAll(
            validationRules.outputFormatIsKnown(configSource.getOutputFormat()));

        final OutputFormat outputFormat = outputFormatLookup.get(configSource.getOutputFormat());

        final Profile profile = validateAndLoadProfile(configSource.getProfileFile());

        if (configSource.shouldViolate()) {
            Path outputPath = configSource.getOutputPath();

            MultiDatasetOutputTarget outputTarget =
                validateAndCreateViolationDirectoryOutputTarget(outputPath, outputFormat, profile);

            violationGenerationEngine.generateDataSet(profile, outputTarget);
        } else {
            Path outputPath = configSource.getOutputPath();

            SingleDatasetOutputTarget outputTarget =
                configSource.isEnableTracing()
                ? validateAndCreateFileOutputTargetWithTracing(outputPath, outputFormat)
                : validateAndCreateFileOutputTarget(outputPath, outputFormat);

            standardGenerationEngine.generateDataSet(profile, outputTarget);
        }
    }

    private Profile validateAndLoadProfile(File profileFile) {
        validator.validateAll(
            validationRules.profilePathHasNoInvalidCharacters(profileFile),
            validationRules.profileExists(profileFile),
            validationRules.profilePathPointsAtAFile(profileFile),
            validationRules.profileFileIsNotEmpty(profileFile),
            validationRules.profileMatchesJsonSchema(profileFile));

        final Profile profile;
        try {
            profile = profileReader.read(profileFile.toPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        validator.validateAll(
            validationRules.profileAssignsTypesToAllFields(profile)
                .onlyIf(configSource.requireFieldTyping()));

        return profile;
    }

    private MultiDatasetOutputTarget validateAndCreateViolationDirectoryOutputTarget(
        Path outputPath,
        OutputFormat format,
        Profile profile) {

        validator.validateAll(
            validationRules.outputDirectoryExistsOrCanBeCreated(outputPath),
            validationRules.outputPathIsNotAFile(outputPath),
            validationRules.outputDirectoryHasRoomForViolationFiles(outputPath, profile.fields.size())
                .unless(configSource.overwriteOutputFiles()));

        return new ViolationDirectoryOutputTarget(outputPath, format, fileUtils);
    }

    private SingleDatasetOutputTarget validateAndCreateFileOutputTarget(
        Path outputPath,
        OutputFormat format) {

        validator.validateAll(
            validationRules.outputFileIsAbsent(outputPath)
                .unless(configSource.overwriteOutputFiles()),
            validationRules.outputPathAncestryExistsOrCanBeCreated(outputPath),
            validationRules.outputPathIsNotADirectory(outputPath));

        return fileUtils.createFileTarget(outputPath, format);
    }

    private SingleDatasetOutputTarget validateAndCreateFileOutputTargetWithTracing(
        Path outputPath,
        OutputFormat format) {

        return new SplittingOutputTarget(
            validateAndCreateFileOutputTarget(
                outputPath,
                format),
            validateAndCreateFileOutputTarget(
                createTraceFilePath(outputPath),
                new TraceOutputFormat()));
    }

    private static Path createTraceFilePath(Path path) {
        return FileUtilsImpl.addFilenameSuffix(
            FileUtilsImpl.replaceExtension(
                path,
                "json"),
            "-trace");
    }
}
