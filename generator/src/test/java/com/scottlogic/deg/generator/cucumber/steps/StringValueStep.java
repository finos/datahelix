package com.scottlogic.deg.generator.cucumber.steps;

import cucumber.api.java.en.When;

public class StringValueStep {

    private DegTestState state;

    public StringValueStep(DegTestState state){
        this.state = state;
    }

    @When("{fieldVar} is {stringValueOperation} {string}")
    public void whenFieldIsConstrainedByTextValue(String fieldName, String constraintName, String value) throws Exception {
        this.state.addConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is not {stringValueOperation} {string}")
    public void whenFieldIsNotConstrainedByTextValue(String fieldName, String constraintName, String value) throws Exception {
        this.state.addNotConstraint(fieldName, constraintName, value);
    }
}
