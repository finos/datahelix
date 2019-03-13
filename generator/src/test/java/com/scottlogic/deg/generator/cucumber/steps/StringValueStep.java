package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.cucumber.utils.CucumberTestHelper;
import com.scottlogic.deg.generator.cucumber.utils.CucumberTestState;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StringValueStep {

    private final CucumberTestState state;
    private final CucumberTestHelper helper;

    public StringValueStep(CucumberTestState state, CucumberTestHelper helper){
        this.state = state;
        this.helper = helper;
    }

    @When("{fieldVar} is {operator} {string}")
    public void whenFieldIsConstrainedByTextValue(String fieldName, String constraintName, String value) throws Exception {
        state.addConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is anything but {operator} {string}")
    public void whenFieldIsNotConstrainedByTextValue(String fieldName, String constraintName, String value) throws Exception {
        state.addNotConstraint(fieldName, constraintName, value);
    }

    @Then("{fieldVar} contains string data")
    public void producedDataShouldContainStringValuesForField(String fieldName){
        helper.assertFieldContainsNullOrMatching(fieldName, String.class);
    }

    @Then("{fieldVar} contains strings of length between {int} and {int} inclusively")
    public void producedDataShouldContainStringValuesInRangeForField(String fieldName, int minInclusive, int maxInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            String.class,
            value -> isLongerThanOrEqual(value, minInclusive) && isShorterThanOrEqual(value, maxInclusive));
    }

    private boolean isShorterThanOrEqual(String value, int maxLengthInclusive) {
        return value.length() <= maxLengthInclusive;
    }

    private boolean isLongerThanOrEqual(String value, int minLengthInclusive) {
        return value.length() >= minLengthInclusive;
    }
}
