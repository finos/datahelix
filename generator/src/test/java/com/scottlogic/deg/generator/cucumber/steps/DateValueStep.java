package com.scottlogic.deg.generator.cucumber.steps;

import cucumber.api.java.en.When;

public class DateValueStep {

    private DegTestState state;

    public DateValueStep(DegTestState state){
        this.state = state;
    }

    @When("{fieldVar} is {dateValueOperation} {dateString}")
    public void whenFieldIsConstrainedByDateValue(String fieldName, String constraintName, String value) throws Exception {
        this.state.addConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is not {dateValueOperation} {dateString}")
    public void whenFieldIsNotConstrainedByDateValue(String fieldName, String constraintName, String value) throws Exception {
        this.state.addNotConstraint(fieldName, constraintName, value);
    }
}
