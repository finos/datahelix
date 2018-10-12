package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.cucumber.utils.DegTestHelper;
import com.scottlogic.deg.generator.cucumber.utils.DegTestState;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import cucumber.api.java.Before;
import cucumber.api.java.en.*;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

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

    @And("^(.+) is null$")
    public void fieldIsNull(String fieldName) throws Exception{
        this.state.addConstraint(fieldName, "null", null);
    }

    @And("^(.+) is not null$")
    public void fieldIsNotNull(String fieldName) throws Exception{
        this.state.addNotConstraint(fieldName, "null", null);
    }

    @But("the profile is invalid as (.+) can't be ([a-z ]+) (((\".*\")|([0-9]+(.[0-9]+){1}))+)")
    public void fieldIsInvalid(String fieldName, String constraint, String value) {
        Object parsedValue;
        if (value.startsWith("\"") && value.endsWith("\"")) {
            parsedValue = value.substring(1, value.length() - 1);
        }  else if (value.contains(".")){
            parsedValue = Double.parseDouble(value);
        } else {
            parsedValue = Integer.parseInt(value);
        }

        try {
            this.state.addConstraint(fieldName, constraint, parsedValue);
            Assert.fail("Expected invalid profile");
        } catch (Exception e) {
            this.state.addException(e);
        }
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
        List <List<String>> data = testHelper.generateAndGetData();
        List <List<String>> expectedRowsOfResults = expectedResultsTable
            .stream()
            .map(row -> new ArrayList<>(row.values()))
            .collect(Collectors.toList());

        Assert.assertThat(data, containsInAnyOrder(expectedRowsOfResults.toArray()));
    }

}
