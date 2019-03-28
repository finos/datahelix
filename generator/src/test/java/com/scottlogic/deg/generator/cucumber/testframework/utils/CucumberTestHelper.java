package com.scottlogic.deg.generator.cucumber.testframework.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.GenerateExecute;
import com.scottlogic.deg.generator.guice.BaseModule;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Responsible for generating data in cucumber tests.
 */
public class CucumberTestHelper {
    private final CucumberTestState testState;

    public CucumberTestHelper(CucumberTestState testState){
        this.testState = testState;
    }

    public void runGenerationProcess() {
        if (testState.generationHasAlreadyOccured) {
            return;
        }

        try {
            Module concatenatedModule =
                Modules
                    .override(new BaseModule(new CucumberGenerationConfigSource(testState)))
                    .with(new CucumberTestModule(testState));

            Injector injector = Guice.createInjector(concatenatedModule);

            injector.getInstance(GenerateExecute.class).run();
        } catch (Exception e) {
            testState.addException(e);
        }

        testState.generationHasAlreadyOccured = true;
    }

    public List<List<Object>> generateAndGetData() {
        if (testState.shouldSkipGeneration()) {
            throw new RuntimeException(
                "Gherkin error: Don't use profile validity steps in conjunction with data checking steps");
        }

        if (testState.dataGenerationType == null) {
            throw new RuntimeException("Gherkin error: Please specify the data strategy");
        }

        if (!generatorHasRun() && testState.testExceptions.isEmpty()) {
            runGenerationProcess();
        }

        return testState.generatedObjects == null
            ? new ArrayList<>()
            : testState.generatedObjects;
    }

    public void runChecksWithoutGeneratingData() {
        testState.disableGeneration();
        runGenerationProcess();
    }

    public boolean generatorHasRun(){
        return testState.generationHasAlreadyOccured;
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

    public Stream<String> getProfileValidationErrors(){
        return Stream.concat(
            testState.testExceptions.stream()
                .filter(e -> e instanceof InvalidProfileException)
                .map(Throwable::getMessage),
            testState.validationReporter
                .getRecordedAlerts().stream()
                .map(a -> String.format(
                    "Field [%s]: %s",
                    a.getField().name,
                    a.getMessage().getVerboseMessage())));
    }

    public Stream<String> getProfileValidationErrorsForField(String fieldName){
        return testState.validationReporter
            .getRecordedAlerts().stream()
            .filter(a -> a.getField().name.equals(fieldName))
            .map(a -> a.getMessage().getVerboseMessage());
    }

    public <T> void assertFieldContainsNullOrMatching(String fieldName, Class<T> clazz){
        assertFieldContainsNullOrMatching(fieldName, clazz, value -> true);
    }

    public <T> void assertFieldContainsNullOrMatching(String fieldName, Class<T> clazz, Function<T, Boolean> predicate){
        assertFieldContains(fieldName, objectValue -> {
            if (objectValue == null){
                return true;
            }

            if (!clazz.isInstance(objectValue)){
                return false; //not the correct type
            }

            //noinspection unchecked
            return predicate.apply((T)objectValue);
        });
    }

    public <T> void assertFieldContainsNullOrNotMatching(String fieldName, Class<T> clazz){
        assertFieldContains(fieldName, objectValue -> {
            if (objectValue == null){
                return true;
            }

            if (clazz.isInstance(objectValue)){
                return false; //matches, but shouldn't match the type
            }

            return true;
        });
    }

    public void assertFieldContains(String fieldName, Function<Object, Boolean> predicate){
        Optional<Integer> fieldIndex = getIndexOfField(fieldName);
        if (!fieldIndex.isPresent()){
            throw new IllegalArgumentException(String.format("Field [%s] has not been defined", fieldName));
        }

        List<List<Object>> allData = this.generateAndGetData();
        List<Object> dataForField = allData.stream().map(row -> row.get(fieldIndex.get())).collect(Collectors.toList());

        Assert.assertThat(
            dataForField,
            new ListPredicateMatcher(predicate));
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

