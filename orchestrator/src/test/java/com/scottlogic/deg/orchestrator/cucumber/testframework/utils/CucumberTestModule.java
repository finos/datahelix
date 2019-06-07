package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.generation.DataGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.validation.MultipleProfileValidator;
import com.scottlogic.deg.output.outputtarget.OutputTargetFactory;
import com.scottlogic.deg.output.outputtarget.SingleDatasetOutputTarget;
import com.scottlogic.deg.profile.reader.ProfileReader;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.TypingRequiredPerFieldValidator;
import com.scottlogic.deg.output.manifest.ManifestWriter;
import com.scottlogic.deg.orchestrator.validator.ConfigValidator;
import com.scottlogic.deg.generator.validators.ErrorReporter;

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
        bind(CucumberTestState.class).toInstance(testState);
        bind(ProfileReader.class).to(CucumberProfileReader.class);
        bind(GenerationConfigSource.class).to(CucumberGenerationConfigSource.class);
        if (testState.requireFieldTyping) {
            // This binding overrides the requireFieldTyping config option, so an alternative
            // (MultipleProfileValidator) needs to be used when field typing is not required.
            bind(ProfileValidator.class).to(TypingRequiredPerFieldValidator.class);
        } else {
            bind(ProfileValidator.class).to(MultipleProfileValidator.class);
        }
        bind(ErrorReporter.class).toInstance(new CucumberErrorReporter(testState));
        bind(DecisionTreeFactory.class).to(CucumberDecisionTreeFactory.class);

        bind(ConfigValidator.class).toInstance(mock(ConfigValidator.class));
        bind(ManifestWriter.class).toInstance(mock(ManifestWriter.class));
        bind(SingleDatasetOutputTarget.class).toInstance(new InMemoryOutputTarget(testState));

        OutputTargetFactory mockOutputTargetFactory = mock(OutputTargetFactory.class);
        when(mockOutputTargetFactory.create(any())).thenReturn(new InMemoryOutputTarget(testState));
        bind(OutputTargetFactory.class).toInstance(mockOutputTargetFactory);

        if (testState.shouldSkipGeneration()) {
            DataGenerator mockDataGenerator = mock(DataGenerator.class);
            when(mockDataGenerator.generateData(any())).thenReturn(Stream.empty());
            bind(DataGenerator.class).toInstance(mockDataGenerator);
        }
    }
}
