package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.cucumber.utils.DegTestState;
import cucumber.api.java.en.When;

import java.math.BigDecimal;

public class NumericValueStep {

    private DegTestState state;
    public NumericValueStep(DegTestState state){
        this.state = state;
    }

    @When("{fieldVar} is {operator} {int}")
    public void whenFieldIsConstrainedByNumericValue(String fieldName, String constraintName, int value) throws Exception {
        this.state.addConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is not {operator} {int}")
    public void whenFieldIsNotConstrainedByNumericValue(String fieldName, String constraintName, int value) throws Exception {
        this.state.addNotConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is {operator} {double}")
    public void whenFieldIsConstrainedByNumericValue(String fieldName, String constraintName, double value) throws Exception {
        this.state.addConstraint(fieldName, constraintName, convertDoubleToBigDecimal(value));
    }

    @When("{fieldVar} is not {operator} {double}")
    public void whenFieldIsNotConstrainedByNumericValue(String fieldName, String constraintName, double value) throws Exception {
        this.state.addNotConstraint(fieldName, constraintName, convertDoubleToBigDecimal(value));
    }

    private BigDecimal convertDoubleToBigDecimal(double value){
        return new BigDecimal(String.format("%f", value)).stripTrailingZeros();
    }
}
