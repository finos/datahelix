package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.cucumber.utils.TestState;
import cucumber.api.java.en.When;

public class RegexValueStep {

    private TestState state;

    public RegexValueStep(TestState state){
        this.state = state;
    }

    @When("{fieldVar} is {operator} {regex}")
    public void whenFieldIsConstrainedByRegex(String fieldName, String constraintName, String value) throws Exception {
        this.state.addConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is anything but {operator} {regex}")
    public void whenFieldIsNotConstrainedByRegex(String fieldName, String constraintName, String value) throws Exception {
        this.state.addNotConstraint(fieldName, constraintName, value);
    }
}
