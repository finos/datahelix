package com.scottlogic.deg.generator.cucumber.testframework.steps;

import com.fasterxml.jackson.core.JsonParseException;
import com.scottlogic.deg.generator.cucumber.testframework.utils.*;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.schemas.v0_1.AtomicConstraintType;
import cucumber.api.java.Before;
import cucumber.api.java.en.*;
import org.hamcrest.Matcher;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;

public class GeneralTestStep {

    private final CucumberTestState state;
    private CucumberTestHelper cucumberTestHelper;

    public GeneralTestStep(CucumberTestState state){
        this.state = state;
    }

    @Before
    public void BeforeEach() {
        this.state.clearState();
        this.cucumberTestHelper = new CucumberTestHelper(state);
    }

    @Given("there is a field (.+)$")
    public void thereIsAField(String fieldName) {
        this.state.addField(fieldName);
    }

    @Given("^the following fields exist:$")
    public void thereAreFields(List<String> fields) {
        fields.forEach(this::thereIsAField);
    }

    @When("the generation strategy is {generationStrategy}")
    public void setTheGenerationStrategy(GenerationConfig.DataGenerationType strategy) {
        this.state.dataGenerationType = strategy;
    }

    @When("the combination strategy is {combinationStrategy}")
    public void setTheCombinationStrategy(GenerationConfig.CombinationStrategyType strategy) {
        this.state.combinationStrategyType = strategy;
    }

    @When("we do not violate any {operator} constraints")
    public void constraintTypeIsNotViolated(String operator){
        this.state.addConstraintToNotViolate(AtomicConstraintType.fromText(operator));
    }

    @When("the walker type is {walkerType}")
    public void setTheCombinationStrategy(GenerationConfig.TreeWalkerType walkerType) {
        this.state.walkerType = walkerType;
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
    public void fieldIsNull(String fieldName) throws Exception{
        this.state.addConstraint(fieldName, "null", null);
    }

    @And("^(.+) is anything but null$")
    public void fieldIsNotNull(String fieldName) throws Exception{
        this.state.addNotConstraint(fieldName, "null", null);
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

    @Then("^the profile is invalid$")
    public void theProfileIsInvalid() {
        cucumberTestHelper.generateAndGetData();

        Assert.assertThat(
            "Expected invalid profile",
            this.cucumberTestHelper.getThrownExceptions(),
            hasItem( //TODO: #802: Stop catching generic errors! We should only be looking for InvalidProfileException here
                either((Matcher)isA(InvalidProfileException.class))
                    .or(isA(JsonParseException.class))
                    .or(isA(IllegalArgumentException.class))
                    .or(isA(ClassCastException.class))));
    }

    @But("^the profile is invalid because \"(.+)\"$")
    public void profileIsInvalidWithError(String expectedError) {
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
                hasItem(expectedError));
        }
    }

    @But("^field (.+?) should fail validation: \"(.+)\"$")
    public void fieldIsInvalidWithError(String fieldName, String expectedError) {
        cucumberTestHelper.runChecksWithoutGeneratingData();

        List<String> errors = this.cucumberTestHelper
            .getProfileValidationErrorsForField(fieldName)
            .collect(Collectors.toList());

        if (errors.size() == 0) {
            Assert.fail("No validation errors were raised");
        } else {
            Assert.assertThat(
                "Expected profile validation error for field " + fieldName,
                errors,
                hasItem(expectedError));
        }
    }

    @Then("^I am presented with an error message$")
    public void dataGeneratorShouldError() {
        cucumberTestHelper.generateAndGetData();

        Assert.assertThat(cucumberTestHelper.generatorHasThrownException(), is(true));
    }

    @And("^no data is created$")
    public void noDataIsCreated() {
        List<List<Object>> data = cucumberTestHelper.generateAndGetData();

        String serialisedData = String.join(
            "\n",
            data
                .stream()
                .map(row ->
                    String.join(
                        ",",
                        row
                            .stream()
                            .map(cell -> cell == null ? "<null>" : cell.toString())
                            .collect(Collectors.toList())))
                .collect(Collectors.toList()));

        Assert.assertThat(
            "Some data was generated when none was expected:\n" + serialisedData,
            data,
            empty());
    }

    @Then("^the following data should be generated:$")
    public void theFollowingDataShouldBeGenerated(List<Map<String, String>> expectedResultsTable) {
        GeneratedTestData data = getExpectedAndGeneratedData(expectedResultsTable);

        assertOutputData(null, data.generatedData, new RowsMatchAnyOrderMatcher(data.expectedData));
    }

    @Then("^the following data should be generated in order:$")
    public void theFollowingDataShouldBeGeneratedInOrder(List<Map<String, String>> expectedResultsTable) {
        GeneratedTestData data = getExpectedAndGeneratedData(expectedResultsTable);

        assertOutputData(null, data.generatedData, equalTo(data.expectedData));
    }

    @Then("^the following data should be included in what is generated:$")
    public void theFollowingDataShouldBeContainedInActual(List<Map<String, String>> expectedResultsTable) {
        GeneratedTestData data = getExpectedAndGeneratedData(expectedResultsTable);

        assertOutputData(null, data.generatedData, new RowsPresentMatcher(data.expectedData));
    }

    @Then("^the following data should not be included in what is generated:$")
    public void theFollowingDataShouldNotBeContainedInActual(List<Map<String, String>> expectedResultsTable) {
        GeneratedTestData data = getExpectedAndGeneratedData(expectedResultsTable);

        assertOutputData(null, data.generatedData, new RowsAbsentMatcher(data.expectedData));
    }

    private void assertOutputData(String message, List<List<Object>> data, Matcher<List<List<Object>>> matcher){
        assertNoGenerationErrors();

        Assert.assertThat(message, data, matcher);
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

    private List <List<Object>> getComparableExpectedResults(List<Map<String, String>> expectedResultsTable) {
        return expectedResultsTable
            .stream()
            .map(row -> new ArrayList<>(row.values()))
            .map(row -> row.stream().map(cell -> {
                try {
                    return GeneratorTestUtilities.parseExpected(cell);
                } catch (JsonParseException | InvalidProfileException e) {
                    state.addException(e);
                    return "<exception thrown: " + e.getMessage() + ">";
                }
            }).collect(Collectors.toList()))
            .collect(Collectors.toList());
    }

    private GeneratedTestData getExpectedAndGeneratedData(List<Map<String, String>> expectedResultsTable){
        List <List<Object>> expectedRowsOfResults = getComparableExpectedResults(expectedResultsTable);
        List <List<Object>> data = cucumberTestHelper.generateAndGetData();
        return new GeneratedTestData(expectedRowsOfResults, data);
    }

    @Then("some data should be generated")
    public void someDataShouldBeGenerated() {
        List <List<Object>> data = cucumberTestHelper.generateAndGetData();

        assertNoGenerationErrors();
        Assert.assertThat("No data was generated but some was expected", data, not(empty()));
    }

    @Then("{long} rows of data are generated")
    public void theExpectedNumberOfRowsAreGenerated(long expectedNumberOfRows) {
        List <List<Object>> data = cucumberTestHelper.generateAndGetData();

        assertNoGenerationErrors();
        Assert.assertThat(
            "Unexpected number of rows returned",
            (long)data.size(),
            equalTo(expectedNumberOfRows));
    }

    @Given("the generator can generate at most {long} rows")
    public void theGeneratorCanGenerateAtMostRows(long maxNumberOfRows) {
        state.maxRows = Optional.of(maxNumberOfRows);
    }

    class GeneratedTestData {
        List <List<Object>> expectedData;
        List <List<Object>> generatedData;

        GeneratedTestData(List <List<Object>> expectedData, List <List<Object>> generatedData){
            this.expectedData = expectedData;
            this.generatedData = generatedData;
        }
    }
}
