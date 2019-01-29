package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.cucumber.utils.CucumberTestState;
import cucumber.api.java.en.When;

public class BooleanValueStep {
    private CucumberTestState state;
    public BooleanValueStep(CucumberTestState state){
        this.state = state;
    }

    @When("{fieldVar} is {operator} {boolean}")
    public void whenFieldIsConstrainedByNumericValue(String fieldName, String constraintName, Boolean value) {
        this.state.addConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is anything but {operator} {boolean}")
    public void whenFieldIsNotConstrainedByNumericValue(String fieldName, String constraintName, Boolean value) {
        this.state.addNotConstraint(fieldName, constraintName, value);
    }
}
