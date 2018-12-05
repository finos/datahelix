package com.scottlogic.deg.generator.cucumber.steps;

import com.fasterxml.jackson.core.JsonParseException;
import com.scottlogic.deg.generator.cucumber.utils.*;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import cucumber.api.java.Before;
import cucumber.api.java.en.*;
import org.hamcrest.Matcher;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;

public class GeneralTestStep {

    private DegTestState state;
    private DegTestHelper testHelper;

    public GeneralTestStep(DegTestState state){
        this.state = state;
    }

    @Before
    public void BeforeEach() {
        this.state.clearState();
        this.testHelper = new DegTestHelper(state);
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
        this.state.generationStrategy = strategy;
    }

    @When("the combination strategy is {combinationStrategy}")
    public void setTheCombinationStrategy(GenerationConfig.CombinationStrategyType strategy) {
        this.state.combinationStrategy = strategy;
    }

    @And("^(.+) is null$")
    public void fieldIsNull(String fieldName) throws Exception{
        this.state.addConstraint(fieldName, "null", null);
    }

    @And("^(.+) is anything but null$")
    public void fieldIsNotNull(String fieldName) throws Exception{
        this.state.addNotConstraint(fieldName, "null", null);
    }

    @But("the profile is invalid as (.+) can't be ([a-z ]+) (((\".*\")|(" + DateValueStep.DATE_REGEX + ")|([0-9]+(.[0-9]+){1}))+)")
    public void fieldIsInvalid(String fieldName, String constraint, String value) {
        try {
            Object parsedValue = GeneratorTestUtilities.parseInput(value);
            this.state.addConstraint(fieldName, constraint, parsedValue);
        } catch (Exception e) {
            this.state.addException(e);
        }

        testHelper.generateAndGetData();

        Assert.assertThat(
            "Expected invalid profile",
            this.testHelper.getThrownExceptions(),
            hasItem(
                either((Matcher)isA(InvalidProfileException.class))
                    .or(isA(JsonParseException.class))
                    .or(isA(IllegalArgumentException.class))
                    .or(isA(ClassCastException.class))));
    }

    @Then("^I am presented with an error message$")
    public void dataGeneratorShouldError() {
        testHelper.generateAndGetData();
        Assert.assertThat(testHelper.generatorHasThrownException(), is(true));
    }

    @And("^no data is created$")
    public void noDataIsCreated() {
        testHelper.generateAndGetData();
        Assert.assertFalse(testHelper.hasDataBeenGenerated());
    }

    @Then("^the following data should be generated:$")
    public void theFollowingDataShouldBeGenerated(List<Map<String, String>> expectedResultsTable) {
        GeneratedTestData data = getExpectedAndGeneratedData(expectedResultsTable);

        Assert.assertThat(
            "Exceptions thrown during generation",
            testHelper.getThrownExceptions(),
            empty());
        Assert.assertThat(data.generatedData, new RowsMatchAnyOrderMatcher(data.expectedData));
    }

    @Then("^the following data should be generated in order:$")
    public void theFollowingDataShouldBeGeneratedInOrder(List<Map<String, String>> expectedResultsTable) {
        GeneratedTestData data = getExpectedAndGeneratedData(expectedResultsTable);

        Assert.assertThat(
            "Exceptions thrown during generation",
            testHelper.getThrownExceptions(),
            empty());
        Assert.assertThat(data.generatedData, contains(data.expectedData));
    }

    @Then("^the following data should be included in what is generated:$")
    public void theFollowingDataShouldBeContainedInActual(List<Map<String, String>> expectedResultsTable) {
        GeneratedTestData data = getExpectedAndGeneratedData(expectedResultsTable);

        Assert.assertThat(
            "Exceptions thrown during generation",
            testHelper.getThrownExceptions(),
            empty());
        Assert.assertThat(data.generatedData, new RowsPresentMatcher(data.expectedData));
    }

    @Then("^the following data should not be included in what is generated:$")
    public void theFollowingDataShouldNotBeContainedInActual(List<Map<String, String>> expectedResultsTable) {
        GeneratedTestData data = getExpectedAndGeneratedData(expectedResultsTable);

        Assert.assertThat(
            "Exceptions thrown during generation",
            testHelper.getThrownExceptions(),
            empty());
        Assert.assertThat(data.generatedData, new RowsAbsentMatcher(data.expectedData));
    }

    private List <List<Object>> getComparableExpectedResults(List<Map<String, String>> expectedResultsTable) {
        return expectedResultsTable
            .stream()
            .map(row -> new ArrayList<>(row.values()))
            .map(row -> row.stream().map(cell -> {
                try {
                    return GeneratorTestUtilities.parseExpected(cell);
                } catch (JsonParseException e) {
                    this.state.addException(e);
                    return "<exception thrown: " + e.getMessage() + ">";
                }
            }).collect(Collectors.toList()))
            .collect(Collectors.toList());
    }

    private GeneratedTestData getExpectedAndGeneratedData(List<Map<String, String>> expectedResultsTable){
        List <List<Object>> expectedRowsOfResults = getComparableExpectedResults(expectedResultsTable);
        List <List<Object>> data = testHelper.generateAndGetData();
        return new GeneratedTestData(expectedRowsOfResults, data);
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
