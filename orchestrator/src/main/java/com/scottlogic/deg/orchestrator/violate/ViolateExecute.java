package com.scottlogic.deg.orchestrator.violate;

import com.google.inject.Inject;
import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.output.writer.DataSetWriter;
import com.scottlogic.deg.output.target.SingleDatasetOutputTarget;
import com.scottlogic.deg.orchestrator.guice.AllConfigSource;
import com.scottlogic.deg.generator.inputs.profileviolation.ProfileViolator;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.output.target.OutputTargetFactory;
import com.scottlogic.deg.output.FileUtils;
import com.scottlogic.deg.generator.validators.ErrorReporter;
import com.scottlogic.deg.orchestrator.validator.ConfigValidator;
import com.scottlogic.deg.profile.reader.ProfileReader;
import com.scottlogic.deg.profile.v0_1.ProfileSchemaValidator;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Stream;

public class ViolateExecute implements Runnable {
    private final ErrorReporter errorReporter;
    private final AllConfigSource configSource;
    private final ConfigValidator configValidator;
    private final OutputTargetFactory outputTargetFactory;
    private final ProfileReader profileReader;
    private final ProfileValidator profileValidator;
    private final ProfileSchemaValidator profileSchemaValidator;
    private final ProfileViolator profileViolator;
    private final DataGenerator dataGenerator;
    private final ViolateOutputValidator violateOutputValidator;

    @Inject
    ViolateExecute(
        ProfileReader profileReader,
        AllConfigSource configSource,
        OutputTargetFactory outputTargetFactory,
        ConfigValidator configValidator,
        ErrorReporter errorReporter,
        ProfileValidator profileValidator,
        ProfileSchemaValidator profileSchemaValidator,
        ProfileViolator profileViolator,
        DataGenerator dataGenerator,
        ViolateOutputValidator violateOutputValidator) {

        this.profileReader = profileReader;
        this.configSource = configSource;
        this.outputTargetFactory = outputTargetFactory;
        this.configValidator = configValidator;
        this.profileSchemaValidator = profileSchemaValidator;
        this.errorReporter = errorReporter;
        this.profileValidator = profileValidator;
        this.profileViolator = profileViolator;
        this.dataGenerator = dataGenerator;
        this.violateOutputValidator = violateOutputValidator;
    }

    @Override
    public void run() {
        try {
            configValidator.preProfileChecks(configSource);
            profileSchemaValidator.validateProfile(configSource.getProfileFile());

            Profile profile = profileReader.read(configSource.getProfileFile().toPath());

            profileValidator.validate(profile);
            violateOutputValidator.validate(profile);

            doGeneration(profile);

        }
        catch (ValidationException e){
            errorReporter.displayValidation(e);
        }
        catch (IOException e) {
            errorReporter.displayException(e);
        }
    }

    private void doGeneration(Profile profile) throws IOException {
        List<Profile> violatedProfiles = profileViolator.violate(profile);
        if (violatedProfiles.isEmpty()) {
            return;
        }

        DecimalFormat intFormatter = FileUtils.getDecimalFormat(violatedProfiles.size());

        int filename = 1;
        for (Profile violatedProfile : violatedProfiles) {
            SingleDatasetOutputTarget outputTarget = outputTargetFactory.create(intFormatter.format(filename++));
            Stream<GeneratedObject> generatedObjectStream = dataGenerator.generateData(violatedProfile);
            outputData(profile, generatedObjectStream, outputTarget);
        }
    }


    private void outputData(Profile profile, Stream<GeneratedObject> generatedDataItems, SingleDatasetOutputTarget outputTarget) throws IOException {
        try (DataSetWriter writer = outputTarget.openWriter(profile.fields)) {
            generatedDataItems.forEach(row -> {
                try {
                    writer.writeRow(row);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
