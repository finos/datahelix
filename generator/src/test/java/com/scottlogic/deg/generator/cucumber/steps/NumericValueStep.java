package com.scottlogic.deg.generator.cucumber.steps;

import com.scottlogic.deg.generator.cucumber.utils.CucumberTestHelper;
import com.scottlogic.deg.generator.cucumber.utils.CucumberTestState;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumericValueStep {

    private final CucumberTestState state;
    private final CucumberTestHelper helper;

    public NumericValueStep(CucumberTestState state, CucumberTestHelper helper){
        this.state = state;
        this.helper = helper;
    }

    @When("{fieldVar} is {operator} {number}")
    public void whenFieldIsConstrainedByNumericValue(String fieldName, String constraintName, Number value) {
        this.state.addConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is anything but {operator} {number}")
    public void whenFieldIsNotConstrainedByNumericValue(String fieldName, String constraintName, Number value) {
        this.state.addNotConstraint(fieldName, constraintName, value);
    }

    @Then("{fieldVar} contains numeric data")
    public void producedDataShouldContainNumericValuesForField(String fieldName){
        this.helper.assertFieldContainsNullOrMatching(fieldName, Number.class);
    }

    @Then("{fieldVar} contains numeric values between {number} and {number} inclusively")
    public void producedDataShouldContainNumericValuesInRangeForField(String fieldName, Number minInclusive, Number maxInclusive){
        this.helper.assertFieldContainsNullOrMatching(
            fieldName,
            Number.class,
            value -> isGreaterThanOrEqual(value, minInclusive) && isLessThanOrEqual(value, maxInclusive));
    }

    private boolean isGreaterThanOrEqual(Number value, Number minInclusive){
        BigDecimal valueAsBigDecimal = coerceToBigDecimal(value);
        BigDecimal minInclusiveAsBigDecimal = coerceToBigDecimal(minInclusive);

        return valueAsBigDecimal.compareTo(minInclusiveAsBigDecimal) >= 0;
    }

    private boolean isLessThanOrEqual(Number value, Number maxInclusive){
        BigDecimal valueAsBigDecimal = coerceToBigDecimal(value);
        BigDecimal maxInclusiveAsBigDecimal = coerceToBigDecimal(maxInclusive);

        return valueAsBigDecimal.compareTo(maxInclusiveAsBigDecimal) <= 0;
    }

    private BigDecimal coerceToBigDecimal(Number number){
        if (number instanceof BigDecimal){
            return (BigDecimal)number;
        }

        if (number instanceof Integer){
            return BigDecimal.valueOf((long)(int)number);
        }

        if (number instanceof BigInteger){
            return new BigDecimal((BigInteger) number);
        }

        if (number instanceof Long){
            return BigDecimal.valueOf((long)number);
        }

        throw new IllegalArgumentException(String.format("Unable to coerce %s of type %s to a BigDecimal", number, number.getClass().getName()));
    }
}
