package com.scottlogic.deg.generator.cucumber.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.scottlogic.deg.generator.*;
import com.scottlogic.deg.generator.guice.BaseModule;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Assert;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Responsible for generating data in cucumber tests.
 */
public class CucumberTestHelper {

    private final CucumberTestState testState;

    public CucumberTestHelper(CucumberTestState testState){
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

    public void assertFieldContainsNullOrMatching(String fieldName, Function<Object, Boolean> predicate){
        Optional<Integer> fieldIndex = getIndexOfField(fieldName);
        if (!fieldIndex.isPresent()){
            throw new IllegalArgumentException(String.format("Field [%s] has not been defined", fieldName));
        }

        List<List<Object>> allData = this.generateAndGetData();
        List<Object> dataForField = allData.stream().map(row -> row.get(fieldIndex.get())).collect(Collectors.toList());

        Assert.assertThat(
            dataForField,
            new ListPredicateMatcher(value -> value == null || predicate.apply(value)));
    }

    private Optional<Integer> getIndexOfField(String fieldName){
        for (int index = 0; index < testState.profileFields.size(); index++){
            Field field = testState.profileFields.get(index);
            if (field.name.equals(fieldName)){
                return Optional.of(index);
            }
        }

        return Optional.empty();
    }
}

