package com.scottlogic.deg.generator.cucumber.steps;

import cucumber.api.java.en.When;

public class RegexStep {

    private DegTestState state;
    private final static String regexOperations = "(matching regex|containing regex)";

    public RegexStep(DegTestState state){
        this.state = state;
    }

    @When("^(.+) is "+ RegexStep.regexOperations + " /(.+)/$")
    public void whenFieldIsConstrainedByRegex(String fieldName, String constraintName, String value) throws Exception {
        this.state.addConstraint(fieldName, constraintName, value);
    }

    @When("^(.+) is not "+ RegexStep.regexOperations + " /(.+)/$")
    public void whenFieldIsNotConstrainedByRegex(String fieldName, String constraintName, String value) throws Exception {
        this.state.addNotConstraint(fieldName, constraintName, value);
    }
}
