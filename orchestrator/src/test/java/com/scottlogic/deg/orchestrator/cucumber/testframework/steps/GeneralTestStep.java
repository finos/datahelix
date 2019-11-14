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

package com.scottlogic.deg.orchestrator.cucumber.testframework.steps;

import com.fasterxml.jackson.core.JsonParseException;
import com.scottlogic.datahelix.generator.core.config.detail.CombinationStrategyType;
import com.scottlogic.datahelix.generator.core.config.detail.DataGenerationType;
import com.scottlogic.deg.orchestrator.cucumber.testframework.utils.*;
import com.scottlogic.deg.profile.dtos.constraints.ConstraintType;

import com.scottlogic.datahelix.generator.common.ValidationException;
import cucumber.api.java.Before;
import cucumber.api.java.en.*;
import org.hamcrest.Matcher;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;

public class GeneralTestStep {

    private final CucumberTestState state;
    private CucumberTestHelper cucumberTestHelper;

    public GeneralTestStep(CucumberTestState state) {
        this.state = state;
    }

    @Before
    public void BeforeEach() {
        this.cucumberTestHelper = new CucumberTestHelper(state);
    }

    @Given("there is a nullable field (.+)$")
    public void thereIsANullableField(String fieldName) {
        this.state.addNullableField(fieldName);
    }

    @Given("there is a non nullable field (.+)$")
    public void thereIsANonNullableField(String fieldName) {
        this.state.addNonNullableField(fieldName);
    }

    @Given("^the following nullable fields exist:$")
    public void thereAreNullableFields(List<String> fields) {
        fields.forEach(this::thereIsANullableField);
    }

    @Given("^the following non nullable fields exist:$")
    public void thereAreNonNullableFields(List<String> fields) {
        fields.forEach(this::thereIsANonNullableField);
    }

    @When("the generation strategy is {generationStrategy}")
    public void setTheGenerationStrategy(DataGenerationType strategy) {
        this.state.dataGenerationType = strategy;
    }

    @When("the combination strategy is {combinationStrategy}")
    public void setTheCombinationStrategy(CombinationStrategyType strategy) {
        this.state.combinationStrategyType = strategy;
    }

    @When("we do not violate any {operator} constraints")
    public void constraintTypeIsNotViolated(String operator) {
        this.state.addConstraintToNotViolate(ConstraintType.fromName(operator));
    }

    @Given("the data requested is {generationMode}")
    public void setTheGenerationMode(CucumberGenerationMode generationMode) {
        switch (generationMode) {
            case VIOLATING:
                state.shouldViolate = true;
                break;
            case VALIDATING:
                state.shouldViolate = false;
                break;
            default:
                throw new IllegalArgumentException("Specified generation mode not supported");
        }
    }

    @And("^(.+) is null$")
    public void fieldIsNull(String fieldName) throws Exception {
        this.state.addConstraint(fieldName, ConstraintType.IS_NULL, true);
    }

    @And("^(.+) is anything but null$")
    public void fieldIsNotNull(String fieldName) throws Exception {
        this.state.addNotConstraint(fieldName, ConstraintType.IS_NULL, true);
    }

    @And("^(.+) is unique$")
    public void uniquefieldIsUnique(String fieldName) {
        this.state.setFieldUnique(fieldName);
    }

    @And("^(.+) is equal to field (.+)$")
    public void fieldEqualTo(String field, String otherField) {
        state.addRelationConstraint(field, ConstraintType.EQUAL_TO_FIELD, otherField);
    }

    @When("^If and Then are described below$")
    public void ifStartThen() {
        state.startCreatingIfConstraint(2);
    }

    @When("^If Then and Else are described below$")
    public void ifStartThenElse() {
        state.startCreatingIfConstraint(3);
    }

    @And("All Of the next {number} constraints")
    public void allOf(int count) {
        state.startCreatingAllOfConstraint(count);
    }

    @And("Any Of the next {number} constraints")
    public void anyOf(int count) {
        state.startCreatingAnyOfConstraint(count);
    }

    @Then("^the profile should be considered valid$")
    public void theProfileIsValid() {
        cucumberTestHelper.runChecksWithoutGeneratingData();

        List<String> errors = this.cucumberTestHelper
            .getProfileValidationErrors()
            .collect(Collectors.toList());

        Assert.assertThat(
            "There were unexpected profile validation errors",
            errors,
            empty());
    }

    @But("^the profile is invalid because \"(.+)\"$")
    public void profileIsInvalidWithError(String expectedError) {
        state.expectExceptions = true;
        cucumberTestHelper.generateAndGetData();

        List<String> errors = this.cucumberTestHelper
            .getProfileValidationErrors()
            .collect(Collectors.toList());

        if (errors.size() == 0) {
            Assert.fail("No profile validation errors were raised");
        } else {
            Assert.assertThat(
                "Expected profile validation error",
                errors,
                hasItem(matchesPattern(expectedError)));
        }
    }

    @But("^the profile is invalid")
    public void profileIsInvalid()
    {
        state.expectExceptions = true;
        cucumberTestHelper.generateAndGetData();

        List<String> errors = this.cucumberTestHelper
            .getProfileValidationErrors()
            .collect(Collectors.toList());
        Assert.assertFalse(errors.isEmpty());

    }

    @But("^the profile is invalid with error \"(.+)\"$")
    public void profileIsInvalidWithErrorMessage(String expectedError) {
        state.expectExceptions = true;
        cucumberTestHelper.generateAndGetData();

        List<String> errors = this.cucumberTestHelper
            .getProfileValidationErrors()
            .collect(Collectors.toList());

        if (errors.size() == 0) {
            Assert.fail("No profile validation errors were raised");
        } else {
            Assert.assertEquals(errors.get(0), expectedError);
        }
    }

    @But("^the profile is invalid with error containing \"(.+)\"$")
    public void profileIsInvalidWithErrorContainingErrorMessage(String expectedError) {
        state.expectExceptions = true;
        cucumberTestHelper.generateAndGetData();

        List<String> errors = this.cucumberTestHelper
            .getProfileValidationErrors()
            .collect(Collectors.toList());

        if (errors.size() == 0) {
            Assert.fail("No profile validation errors were raised");
        } else {
            Assert.assertTrue(errors.get(0).contains(expectedError));
        }
    }

    @And("^no data is created$")
    public void noDataIsCreated() {
        List<Map<String,Object>> data = cucumberTestHelper.generateAndGetData();

        String serialisedData = data
            .stream()
            .map(row ->
                row.values()
                    .stream()
                    .map(cell -> cell == null ? "<null>" : cell.toString())
                    .collect(Collectors.joining(",")))
            .collect(Collectors.joining("\n"));

        Assert.assertThat(
            "Some data was generated when none was expected:\n" + serialisedData,
            data,
            empty());
    }

    @Then("^the following data should be generated:$")
    public void theFollowingDataShouldBeGenerated(List<Map<String, String>> expectedResultsTable) {
        GeneratedTestData data = getExpectedAndGeneratedData(expectedResultsTable);
        assertOutputData(data.generatedData, new RowsMatchAnyOrderMatcher(data.expectedData));
    }

    @Then("^the following data should be generated in order:$")
    public void theFollowingDataShouldBeGeneratedInOrder(List<Map<String, String>> expectedResultsTable) {
        GeneratedTestData data = getExpectedAndGeneratedData(expectedResultsTable);

        assertOutputData(data.generatedData, equalTo(data.expectedData));
    }

    @Then("^the following data should be included in what is generated:$")
    public void theFollowingDataShouldBeContainedInActual(List<Map<String, String>> expectedResultsTable) {
        GeneratedTestData data = getExpectedAndGeneratedData(expectedResultsTable);

        assertOutputData(data.generatedData, new RowsPresentMatcher(data.expectedData));
    }

    @Then("^the following data should not be included in what is generated:$")
    public void theFollowingDataShouldNotBeContainedInActual(List<Map<String, String>> expectedResultsTable) {
        GeneratedTestData data = getExpectedAndGeneratedData(expectedResultsTable);

        assertOutputData(data.generatedData, new RowsAbsentMatcher(data.expectedData));
    }

    private void assertOutputData(List<Map<String, Object>> data, Matcher<List<Map<String, Object>>> matcher) {
        assertNoGenerationErrors();

        Assert.assertThat(data, matcher);
    }

    private void assertNoGenerationErrors() {
        Assert.assertThat(
            "Exceptions thrown during generation",
            cucumberTestHelper.getThrownExceptions(),
            empty());

        Assert.assertThat(
            "Validation errors thrown during generation",
            cucumberTestHelper.getProfileValidationErrors().collect(Collectors.toList()),
            empty());
    }

    private List<Map<String, Object>> getComparableExpectedResults(List<Map<String, String>> expectedResultsTable) {
        return expectedResultsTable
            .stream()
            .map(row ->
            {
                Map<String, Object> rowMap = new HashMap<>();
                row.keySet().forEach(key ->
                {
                    try
                    {
                        rowMap.put(key, GeneratorTestUtilities.parseExpected(row.get(key)));
                    } catch (JsonParseException | ValidationException e)
                    {
                        state.addException(e);
                        rowMap.put(key, "<exception thrown: " + e.getMessage() + ">");
                    }
                });
                return rowMap;
            })
            .collect(Collectors.toList());
    }

    private GeneratedTestData getExpectedAndGeneratedData(List<Map<String, String>> expectedResultsTable) {
        List<Map<String, Object>> expectedRowsOfResults = getComparableExpectedResults(expectedResultsTable);
        List<Map<String, Object>> data = cucumberTestHelper.generateAndGetData();
        return new GeneratedTestData(expectedRowsOfResults, data);
    }

    @Then("some data should be generated")
    public void someDataShouldBeGenerated() {
        List<Map<String, Object>> data = cucumberTestHelper.generateAndGetData();

        assertNoGenerationErrors();
        Assert.assertThat("No data was generated but some was expected", data, not(empty()));
    }

    @Then("{long} row(s) of data is/are generated")
    public void theExpectedNumberOfRowsAreGenerated(long expectedNumberOfRows) {
        List<Map<String, Object>> data = cucumberTestHelper.generateAndGetData();

        assertNoGenerationErrors();
        Assert.assertThat(
            "Unexpected number of rows returned",
            (long) data.size(),
            equalTo(expectedNumberOfRows));
    }

    @Given("the generator can generate at most {long} row(s)")
    public void theGeneratorCanGenerateAtMostRows(long maxNumberOfRows) {
        state.maxRows = maxNumberOfRows;
    }

    @And("^(.+) has formatting \"(.+)\"$")
    public void fooHasFormattingFormat(String fieldName, String formatting) {
        state.setFieldFormatting(fieldName, formatting);
    }

    @And("^(.+) has type \"(.+)\"$")
    public void fooHasType(String fieldName, String type) {
        state.setFieldType(fieldName, type);
    }

    static class GeneratedTestData {
        List<Map<String, Object>> expectedData;
        List<Map<String, Object>> generatedData;

        GeneratedTestData(List<Map<String, Object>> expectedData, List<Map<String, Object>> generatedData) {
            this.expectedData = expectedData;
            this.generatedData = generatedData;
        }
    }
}
