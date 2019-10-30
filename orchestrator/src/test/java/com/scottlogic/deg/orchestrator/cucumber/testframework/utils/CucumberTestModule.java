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

package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import com.google.inject.AbstractModule;
import com.scottlogic.deg.generator.generation.AbstractDataGeneratorMonitor;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.generation.NoopDataGeneratorMonitor;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.UniquenessValidator;
import com.scottlogic.deg.generator.validators.ErrorReporter;
import com.scottlogic.deg.orchestrator.violate.manifest.ManifestWriter;
import com.scottlogic.deg.output.outputtarget.OutputTargetFactory;
import com.scottlogic.deg.output.outputtarget.SingleDatasetOutputTarget;
import com.scottlogic.deg.profile.NoopVersionChecker;
import com.scottlogic.deg.profile.SchemaVersionValidator;
import com.scottlogic.deg.profile.reader.FileReader;
import com.scottlogic.deg.profile.reader.ProfileReader;
import com.scottlogic.deg.profile.reader.ConfigValidator;

import java.util.stream.Stream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class which defines bindings for Guice injection specific for cucumber testing. The test state is persisted through
 * the various classes by binding the CucumberTestState object to the instance specified here.
 */
public class CucumberTestModule extends AbstractModule {
    private final CucumberTestState testState;

    public CucumberTestModule(CucumberTestState testState) {
        this.testState = testState;
    }


    @Override
    public void configure() {
        bind(GenerationConfigSource.class).to(CucumberGenerationConfigSource.class);

        bind(CucumberTestState.class).toInstance(testState);
        bind(ProfileReader.class).to(CucumberProfileReader.class);
        bind(SchemaVersionValidator.class).to(NoopVersionChecker.class);
        bind(FileReader.class).to(CucumberFileReader.class);

        bind(ProfileValidator.class).toInstance(new UniquenessValidator(new CucumberGenerationConfigSource(testState)));
        bind(ErrorReporter.class).toInstance(new CucumberErrorReporter(testState));

        bind(ConfigValidator.class).toInstance(mock(ConfigValidator.class));
        bind(ManifestWriter.class).toInstance(mock(ManifestWriter.class));
        bind(SingleDatasetOutputTarget.class).toInstance(new InMemoryOutputTarget(testState));
        bind(AbstractDataGeneratorMonitor.class).to(NoopDataGeneratorMonitor.class);

        OutputTargetFactory mockOutputTargetFactory = mock(OutputTargetFactory.class);
        when(mockOutputTargetFactory.create(any())).thenReturn(new InMemoryOutputTarget(testState));
        bind(OutputTargetFactory.class).toInstance(mockOutputTargetFactory);

        if (testState.shouldSkipGeneration) {
            DataGenerator mockDataGenerator = mock(DataGenerator.class);
            when(mockDataGenerator.generateData(any())).thenReturn(Stream.empty());
            bind(DataGenerator.class).toInstance(mockDataGenerator);
        }
    }
}
