package com.scottlogic.deg.generator.cucumber.testframework.utils;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.scottlogic.deg.generator.ConfigSource;
import com.scottlogic.deg.generator.GenerationEngine;
import com.scottlogic.deg.generator.StandardGenerationEngine;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.inputs.ProfileReader;
import com.scottlogic.deg.generator.inputs.validation.ProfileValidator;
import com.scottlogic.deg.generator.inputs.validation.TypingRequiredPerFieldValidator;
import com.scottlogic.deg.generator.inputs.validation.reporters.ProfileValidationReporter;
import com.scottlogic.deg.generator.outputs.manifest.ManifestWriter;
import com.scottlogic.deg.generator.outputs.targets.OutputTarget;
import com.scottlogic.deg.generator.validators.ConfigValidator;
import com.scottlogic.deg.generator.validators.ErrorReporter;
import com.scottlogic.deg.generator.violations.ViolationGenerationEngine;

import static org.mockito.Mockito.mock;

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
        bind(ConfigSource.class).to(GenerationConfigSource.class);
        bind(GenerationConfigSource.class).to(CucumberGenerationConfigSource.class);
        bind(OutputTarget.class).to(InMemoryOutputTarget.class).in(Singleton.class);
        bind(ManifestWriter.class).to(CucumberManifestWriter.class);
        bind(ConfigValidator.class).to(CucumberGenerationConfigValidator.class);
        bind(ProfileValidationReporter.class).toInstance(testState.validationReporter);
        bind(ProfileValidator.class).to(TypingRequiredPerFieldValidator.class);
        bind(ErrorReporter.class).toInstance(new CucumberErrorReporter(testState));
        bind(DecisionTreeFactory.class).to(CucumberDecisionTreeFactory.class);

        if (testState.shouldSkipGeneration()) {
            bind(GenerationEngine.class).toInstance(mock(GenerationEngine.class));
        } else if (testState.shouldViolate) {
            bind(GenerationEngine.class).to(ViolationGenerationEngine.class);
        } else {
            bind(GenerationEngine.class).to(StandardGenerationEngine.class);
        }
    }
}
