package com.scottlogic.deg.generator.cucumber.testframework.steps;

import com.scottlogic.deg.generator.cucumber.testframework.utils.CucumberTestHelper;
import com.scottlogic.deg.generator.cucumber.testframework.utils.CucumberTestState;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.math.BigDecimal;
import java.util.function.Function;

import static com.scottlogic.deg.generator.utils.NumberUtils.coerceToBigDecimal;

public class NumericValueStep {

    private final CucumberTestState state;
    private final CucumberTestHelper helper;

    public NumericValueStep(CucumberTestState state, CucumberTestHelper helper){
        this.state = state;
        this.helper = helper;
    }

    @When("{fieldVar} is {operator} {number}")
    public void whenFieldIsConstrainedByNumericValue(String fieldName, String constraintName, Number value) {
        state.addConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is anything but {operator} {number}")
    public void whenFieldIsNotConstrainedByNumericValue(String fieldName, String constraintName, Number value) {
        state.addNotConstraint(fieldName, constraintName, value);
    }

    @Then("{fieldVar} contains numeric data")
    public void producedDataShouldContainNumericValuesForField(String fieldName){
        helper.assertFieldContainsNullOrMatching(fieldName, Number.class);
    }

    @Then("{fieldVar} contains anything but numeric data")
    public void producedDataShouldContainAnythingButStringValuesForField(String fieldName){
        helper.assertFieldContainsNullOrNotMatching(fieldName, Number.class);
    }

    @Then("{fieldVar} contains numeric values between {number} and {number} inclusively")
    public void producedDataShouldContainNumericValuesInRangeForField(String fieldName, Number minInclusive, Number maxInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            Number.class,
            isBetweenInclusively(minInclusive, maxInclusive));
    }

    @Then("{fieldVar} contains numeric values less than or equal to {number}")
    public void producedDataShouldContainNumericValuesLessThanForField(String fieldName, Number lessThanInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            Number.class,
            value -> isLessThanOrEqual(value, lessThanInclusive));
    }

    @Then("{fieldVar} contains numeric values greater than or equal to {number}")
    public void producedDataShouldContainNumericValuesGreaterThanForField(String fieldName, Number greaterThanInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            Number.class,
            value -> isGreaterThanOrEqual(value, greaterThanInclusive));
    }

    @Then("{fieldVar} contains numeric values outside {number} and {number}")
    public void producedDataShouldContainNumericValuesOutOfRangeForField(String fieldName, Number min, Number max){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            Number.class,
            value -> !isBetweenInclusively(min, max).apply(value));
    }

    private Function<Number, Boolean> isBetweenInclusively(Number minInclusive, Number maxInclusive){
        return value -> isGreaterThanOrEqual(value, minInclusive) && isLessThanOrEqual(value, maxInclusive);
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
}
