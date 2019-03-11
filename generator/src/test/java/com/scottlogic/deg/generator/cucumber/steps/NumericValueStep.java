package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.cucumber.utils.CucumberTestHelper;
import com.scottlogic.deg.generator.cucumber.utils.CucumberTestState;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.math.BigInteger;

public class NumericValueStep {

    private final CucumberTestState state;
    private final CucumberTestHelper helper;

    public NumericValueStep(CucumberTestState state, CucumberTestHelper helper){
        this.state = state;
        this.helper = helper;
    }

    @When("{fieldVar} is {operator} {number}")
    public void whenFieldIsConstrainedByNumericValue(String fieldName, String constraintName, Number value) {
        this.state.addConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is anything but {operator} {number}")
    public void whenFieldIsNotConstrainedByNumericValue(String fieldName, String constraintName, Number value) {
        this.state.addNotConstraint(fieldName, constraintName, value);
    }

    @Then("{fieldVar} contains numeric data")
    public void producedDataShouldContainNumericValuesForField(String fieldName){
        this.helper.assertFieldContainsNullOrMatching(fieldName, value -> value instanceof Number);
    }
}
