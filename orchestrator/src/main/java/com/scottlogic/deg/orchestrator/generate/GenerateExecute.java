/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.orchestrator.generate;

import com.google.inject.Inject;
import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.DataGeneratorMonitor;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.walker.RetryLimitReachedException;
import com.scottlogic.deg.orchestrator.guice.AllConfigSource;
import com.scottlogic.deg.orchestrator.validator.ConfigValidator;
import com.scottlogic.deg.output.outputtarget.SingleDatasetOutputTarget;
import com.scottlogic.deg.output.writer.DataSetWriter;
import com.scottlogic.deg.profile.reader.ProfileReader;
import com.scottlogic.deg.profile.v0_1.ProfileSchemaValidator;

import java.io.IOException;
import java.util.stream.Stream;

public class GenerateExecute {
    private final AllConfigSource configSource;
    private final SingleDatasetOutputTarget singleDatasetOutputTarget;
    private final ConfigValidator configValidator;
    private final ProfileReader profileReader;
    private final DataGenerator dataGenerator;
    private final ProfileValidator profileValidator;
    private final DataGeneratorMonitor monitor;
    private final ProfileSchemaValidator profileSchemaValidator;

    @Inject
    GenerateExecute(
        ProfileReader profileReader,
        DataGenerator dataGenerator,
        AllConfigSource configSource,
        SingleDatasetOutputTarget singleDatasetOutputTarget,
        ConfigValidator configValidator,
        ProfileValidator profileValidator,
        ProfileSchemaValidator profileSchemaValidator,
        DataGeneratorMonitor monitor) {
        this.profileReader = profileReader;
        this.dataGenerator = dataGenerator;
        this.configSource = configSource;
        this.singleDatasetOutputTarget = singleDatasetOutputTarget;
        this.configValidator = configValidator;
        this.profileSchemaValidator = profileSchemaValidator;
        this.profileValidator = profileValidator;
        this.monitor = monitor;
    }

    public void execute() throws IOException {
        configValidator.preProfileChecks(configSource);

        Profile profile = profileReader.read(configSource.getProfileFile().toPath());
        profileSchemaValidator.validateProfile(configSource.getProfileFile(), profile.getSchemaVersion());

        profileValidator.validate(profile);
        singleDatasetOutputTarget.validate();

        Stream<GeneratedObject> generatedDataItems = dataGenerator.generateData(profile);

        outputData(profile, generatedDataItems);
    }

    private void outputData(Profile profile, Stream<GeneratedObject> generatedDataItems) throws IOException {
        try (DataSetWriter writer = singleDatasetOutputTarget.openWriter(profile.getFields())) {
            generatedDataItems.forEach(row -> {
                try {
                    writer.writeRow(row);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        catch (RetryLimitReachedException ignored) {
            monitor.addLineToPrintAtEndOfGeneration("");
            monitor.addLineToPrintAtEndOfGeneration("The retry limit for generating data has been hit.");
            monitor.addLineToPrintAtEndOfGeneration("This may mean that a lot or all of the profile is contradictory.");
            monitor.addLineToPrintAtEndOfGeneration("Either fix the profile, or try running the same command again.");
        }
        monitor.endGeneration();
    }
}
