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
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.DataGeneratorMonitor;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.output.outputtarget.SingleDatasetOutputTarget;
import com.scottlogic.deg.output.writer.DataSetWriter;
import com.scottlogic.deg.profile.reader.ValidatingProfileReader;

import java.io.IOException;
import java.util.stream.Stream;

public class GenerateExecute {
    private final SingleDatasetOutputTarget singleDatasetOutputTarget;
    private final ValidatingProfileReader profileReader;
    private final DataGenerator dataGenerator;
    private final ProfileValidator profileValidator;
    private final DataGeneratorMonitor monitor;


    @Inject
    GenerateExecute(
        DataGenerator dataGenerator,
        SingleDatasetOutputTarget singleDatasetOutputTarget,
        ValidatingProfileReader profileReader, ProfileValidator profileValidator,
        DataGeneratorMonitor monitor) {
        this.dataGenerator = dataGenerator;
        this.singleDatasetOutputTarget = singleDatasetOutputTarget;
        this.profileReader = profileReader;
        this.profileValidator = profileValidator;
        this.monitor = monitor;
    }

    public void execute() throws IOException {
        Profile profile = profileReader.read();

        profileValidator.validate(profile);

        Stream<GeneratedObject> generatedDataItems = dataGenerator.generateData(profile);

        outputData(profile, generatedDataItems);
    }

    private void outputData(Profile profile, Stream<GeneratedObject> generatedDataItems) throws IOException {
        singleDatasetOutputTarget.validate();

        try (DataSetWriter writer = singleDatasetOutputTarget.openWriter(profile.getFields())) {
            generatedDataItems.forEach(row -> {
                try {
                    writer.writeRow(row);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        monitor.endGeneration();
    }
}