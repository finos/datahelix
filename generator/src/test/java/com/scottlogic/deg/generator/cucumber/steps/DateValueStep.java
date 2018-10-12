package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.cucumber.utils.DegTestState;
import cucumber.api.java.en.When;

public class DateValueStep {

    private DegTestState state;

    public DateValueStep(DegTestState state){
        this.state = state;
    }

    @When("{fieldVar} is {operator} {dateString}")
    public void whenFieldIsConstrainedByDateValue(String fieldName, String constraintName, String value) throws Exception {
        this.state.addConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is not {operator} {dateString}")
    public void whenFieldIsNotConstrainedByDateValue(String fieldName, String constraintName, String value) throws Exception {
        this.state.addNotConstraint(fieldName, constraintName, value);
    }
}
