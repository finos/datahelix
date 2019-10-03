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

package com.scottlogic.deg.orchestrator.violate;

import com.google.inject.Inject;
import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.orchestrator.violate.violator.ProfileViolator;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.common.util.FileUtils;
import com.scottlogic.deg.orchestrator.violate.manifest.ManifestWriter;
import com.scottlogic.deg.output.outputtarget.OutputTargetFactory;
import com.scottlogic.deg.output.outputtarget.SingleDatasetOutputTarget;
import com.scottlogic.deg.output.writer.DataSetWriter;
import com.scottlogic.deg.profile.reader.ValidatingProfileReader;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Stream;

public class ViolateExecute {
    private final OutputTargetFactory outputTargetFactory;
    private final ProfileValidator profileValidator;
    private final ProfileViolator profileViolator;
    private final DataGenerator dataGenerator;
    private final ViolateOutputValidator violateOutputValidator;
    private final ManifestWriter manifestWriter;
    private final ValidatingProfileReader validatingProfileReader;

    @Inject
    ViolateExecute(
        OutputTargetFactory outputTargetFactory,
        ProfileValidator profileValidator,
        ProfileViolator profileViolator,
        DataGenerator dataGenerator,
        ViolateOutputValidator violateOutputValidator,
        ManifestWriter manifestWriter,
        ValidatingProfileReader validatingProfileReader) {
        this.outputTargetFactory = outputTargetFactory;
        this.profileValidator = profileValidator;
        this.profileViolator = profileViolator;
        this.dataGenerator = dataGenerator;
        this.violateOutputValidator = violateOutputValidator;
        this.manifestWriter = manifestWriter;
        this.validatingProfileReader = validatingProfileReader;
    }

    public void execute() throws IOException {
        Profile profile = validatingProfileReader.read();

        profileValidator.validate(profile);
        violateOutputValidator.validate(profile);

        doGeneration(profile);
    }

    private void doGeneration(Profile profile) throws IOException {
        List<ViolatedProfile> violatedProfiles = profileViolator.violate(profile);
        if (violatedProfiles.isEmpty()) {
            return;
        }
        manifestWriter.writeManifest(violatedProfiles);

        DecimalFormat intFormatter = FileUtils.getDecimalFormat(violatedProfiles.size());

        int filename = 1;
        for (Profile violatedProfile : violatedProfiles) {
            SingleDatasetOutputTarget outputTarget =
                outputTargetFactory.create(intFormatter.format(filename++));
            Stream<GeneratedObject> generatedObjectStream = dataGenerator.generateData(violatedProfile);
            outputData(profile, generatedObjectStream, outputTarget);
        }
    }

    private void outputData(
        Profile profile,
        Stream<GeneratedObject> generatedDataItems,
        SingleDatasetOutputTarget outputTarget) throws IOException
    {
        try (DataSetWriter writer = outputTarget.openWriter(profile.getFields())) {
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