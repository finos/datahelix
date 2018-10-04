package com.scottlogic.deg.generator.cucumber.steps;

import cucumber.api.java.en.When;

public class RegexValueStep {

    private DegTestState state;

    public RegexValueStep(DegTestState state){
        this.state = state;
    }

    @When("{fieldVar} is {regexValueOperation} {regex}")
    public void whenFieldIsConstrainedByRegex(String fieldName, String constraintName, String value) throws Exception {
        this.state.addConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is not {regexValueOperation} {regex}")
    public void whenFieldIsNotConstrainedByRegex(String fieldName, String constraintName, String value) throws Exception {
        this.state.addNotConstraint(fieldName, constraintName, value);
    }
}
