package com.scottlogic.deg.generator.cucumber.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.scottlogic.deg.generator.*;
import com.scottlogic.deg.generator.Guice.BaseModule;
import com.scottlogic.deg.generator.generation.GenerationConfig;

import java.util.Collection;
import java.util.List;

/**
 * Responsible for generating data in cucumber tests.
 */
public class CucumberTestHelper {

    private final TestState testState;

    public CucumberTestHelper(TestState testState){
        this.testState = testState;
    }

    public List <List<Object>> generateAndGetData() {
        if (testState.dataGenerationType == null) {
            throw new RuntimeException("Gherkin error: Please specify the data strategy");
        }

        if (this.generatorHasRun()) {
            return testState.generatedObjects;
        }

        try {
            Module concatenatedModule =
                Modules
                .override(new BaseModule(new CucumberGenerationConfigSource(testState)))
                .with(new CucumberTestModule(testState));

            Injector injector = Guice.createInjector(concatenatedModule);

            injector.getInstance(GenerateExecute.class).run();

            return testState.generatedObjects;
        } catch (Exception e) {
            testState.addException(e);
            return null;
        }
    }

    public boolean generatorHasRun(){
        return testState.generatedObjects != null || generatorHasThrownException();
    }

    public boolean generatorHasThrownException() {
        return testState.testExceptions.size() > 0;
    }

    public boolean hasDataBeenGenerated() {
        return testState.generatedObjects != null && testState.generatedObjects.size() > 0;
    }

    public Collection<Exception> getThrownExceptions(){
        return testState.testExceptions;
    }
}
