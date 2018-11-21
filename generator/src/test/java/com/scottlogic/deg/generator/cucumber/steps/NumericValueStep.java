package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.cucumber.utils.DegTestState;
import cucumber.api.java.en.When;

import java.math.BigDecimal;

public class NumericValueStep {

    private DegTestState state;
    public NumericValueStep(DegTestState state){
        this.state = state;
    }

    @When("{fieldVar} is {operator} {number}")
    public void whenFieldIsConstrainedByNumericValue(String fieldName, String constraintName, Number value) {
        this.state.addConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is anything but {operator} {number}")
    public void whenFieldIsNotConstrainedByNumericValue(String fieldName, String constraintName, Number value) {
        this.state.addNotConstraint(fieldName, constraintName, value);
    }
}
