package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.cucumber.utils.CucumberTestState;
import cucumber.api.java.en.When;

public class NullValueStep {

    private CucumberTestState state;

    public NullValueStep(CucumberTestState state){
        this.state = state;
    }

    @When("{fieldVar} is {operator} null")
    public void whenFieldIsConstrainedByTextValue(String fieldName, String constraintName) {
        this.state.addConstraint(fieldName, constraintName, null);
    }

    @When("{fieldVar} is anything but {operator} null")
    public void whenFieldIsNotConstrainedByTextValue(String fieldName, String constraintName) {
        this.state.addNotConstraint(fieldName, constraintName, null);
    }
}
