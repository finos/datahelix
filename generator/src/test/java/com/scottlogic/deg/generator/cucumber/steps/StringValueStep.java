package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.cucumber.utils.CucumberTestHelper;
import com.scottlogic.deg.generator.cucumber.utils.CucumberTestState;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.math.BigInteger;

public class StringValueStep {

    private final CucumberTestState state;
    private final CucumberTestHelper helper;

    public StringValueStep(CucumberTestState state, CucumberTestHelper helper){
        this.state = state;
        this.helper = helper;
    }

    @When("{fieldVar} is {operator} {string}")
    public void whenFieldIsConstrainedByTextValue(String fieldName, String constraintName, String value) throws Exception {
        this.state.addConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is anything but {operator} {string}")
    public void whenFieldIsNotConstrainedByTextValue(String fieldName, String constraintName, String value) throws Exception {
        this.state.addNotConstraint(fieldName, constraintName, value);
    }

    @Then("{fieldVar} contains string data")
    public void producedDataShouldContainStringValuesForField(String fieldName){
        this.helper.assertFieldContainsNullOrMatching(fieldName, value -> value instanceof String);
    }
}
