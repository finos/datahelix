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

import com.fasterxml.jackson.core.JsonParseException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.orchestrator.generate.GenerateExecute;
import com.scottlogic.deg.orchestrator.violate.ViolateExecute;
import com.scottlogic.deg.orchestrator.violate.ViolateModule;
import org.junit.Assert;

import java.io.IOException;
import java.io.UncheckedIOException;
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

    public CucumberTestHelper(CucumberTestState testState) {
        this.testState = testState;
    }

    public void runGenerationProcess() {
        if (testState.expectExceptions){
            generateAndExpectExceptions();
        }
        else {
            try {
                doGeneration();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    private void generateAndExpectExceptions() {
        try {
            doGeneration();
        } catch (IOException e) {
            testState.addException(e);
        } catch (ValidationException e){
            testState.testExceptions.addAll(e.errorMessages.stream().map(ValidationException::new).collect(Collectors.toList()));
        }
    }

    private void doGeneration() throws IOException {
        if (testState.generationHasAlreadyOccured) {
            return;
        }

        Module concatenatedModule =
            Modules.override(new ViolateModule(new CucumberGenerationConfigSource(testState)))
                .with(new CucumberTestModule(testState));

        Injector injector = Guice.createInjector(concatenatedModule);

        if (testState.shouldViolate) {
            injector.getInstance(ViolateExecute.class).execute();
        } else {
            injector.getInstance(GenerateExecute.class).execute();
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

    public boolean generatorHasRun() {
        return testState.generationHasAlreadyOccured;
    }

    public Collection<Exception> getThrownExceptions() {
        return testState.testExceptions;
    }

    public Stream<String> getProfileValidationErrors() {
        return testState.testExceptions.stream()
            .filter(e -> e instanceof ValidationException || e instanceof JsonParseException)
            .map(Throwable::getMessage);
    }

    public <T> void assertFieldContainsNullOrMatching(String fieldName, Class<T> clazz) {
        assertFieldContainsNullOrMatching(fieldName, clazz, value -> true);
    }

    public <T> void assertFieldContainsNullOrMatching(
        String fieldName,
        Class<T> clazz,
        Function<T, Boolean> predicate
    ){
        assertFieldContainsOnly(fieldName, objectValue -> {
            if (objectValue == null) {
                return true;
            }

            if (!clazz.isInstance(objectValue)) {
                return false;
            }

            //noinspection unchecked
            return predicate.apply((T) objectValue);
        });
    }

    public <T> void assertFieldContainsNullOrNotMatching(String fieldName, Class<T> clazz) {
        assertFieldContainsOnly(fieldName, objectValue -> {
            if (objectValue == null) {
                return true;
            }

            if (clazz.isInstance(objectValue)) {
                return false; //matches, but shouldn't match the type
            }

            return true;
        });
    }

    public void assertFieldContains(String fieldName, Function<Object, Boolean> predicate) {
        Optional<Integer> fieldIndex = getIndexOfField(fieldName);
        if (!fieldIndex.isPresent()) {
            throw new IllegalArgumentException(String.format(
                "Field [%s] has not been defined",
                fieldName
            ));
        }

        List<List<Object>> allData = this.generateAndGetData();
        List<Object> dataForField =
            allData.stream().map(row -> row.get(fieldIndex.get())).collect(Collectors.toList());

        Assert.assertThat(dataForField, new ListPredicateAnyTrueIsSuccessMatcher(predicate));
    }

    public void assertFieldContainsOnly(String fieldName, Function<Object, Boolean> predicate) {
        Optional<Integer> fieldIndex = getIndexOfField(fieldName);
        if (!fieldIndex.isPresent()) {
            throw new IllegalArgumentException(String.format(
                "Field [%s] has not been defined",
                fieldName
            ));
        }

        List<List<Object>> allData = this.generateAndGetData();
        List<Object> dataForField =
            allData.stream().map(row -> row.get(fieldIndex.get())).collect(Collectors.toList());

        Assert.assertThat(dataForField, new ListPredicateMatcher(predicate));
    }

    private Optional<Integer> getIndexOfField(String fieldName) {
        for (int index = 0; index < testState.profileFields.size(); index++) {
            Field field = testState.profileFields.get(index);
            if (field.name.equals(fieldName)) {
                return Optional.of(index);
            }
        }

        return Optional.empty();
    }

    public <T> void assertFieldContainsSomeOf(String fieldName, Class<T> clazz) {
        assertFieldContains(fieldName, objectValue -> {
            if (clazz.isInstance(objectValue)) {
                return true;
            }

            return false; //doesn't match the type when it should.
        });
    }
}
