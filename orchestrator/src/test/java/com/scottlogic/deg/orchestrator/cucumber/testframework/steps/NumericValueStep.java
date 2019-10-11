/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.orchestrator.cucumber.testframework.steps;

import com.scottlogic.deg.orchestrator.cucumber.testframework.utils.CucumberTestHelper;
import com.scottlogic.deg.orchestrator.cucumber.testframework.utils.CucumberTestState;
import com.scottlogic.deg.profile.common.ConstraintType;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

import java.math.BigDecimal;
import java.util.function.Function;

import static com.scottlogic.deg.common.util.NumberUtils.coerceToBigDecimal;
import static com.scottlogic.deg.common.util.NumberUtils.tryParse;

public class NumericValueStep {

    private final CucumberTestState state;
    private final CucumberTestHelper helper;

    public NumericValueStep(CucumberTestState state, CucumberTestHelper helper){
        this.state = state;
        this.helper = helper;
    }

    @And("^([A-z0-9]+) is equal to (-?[0-9\\.]+)$")
    public void equalToNumber(String field, String value){
        state.addConstraint(field, AtomicConstraintType.IS_EQUAL_TO_CONSTANT.getText(), tryParse(value));
    }

    @And("^([A-z0-9]+) is greater than (-?[0-9\\.]+)$")
    public void greaterThanNumber(String field, String value){
        state.addConstraint(field, AtomicConstraintType.IS_GREATER_THAN_CONSTANT.getText(), tryParse(value));
    }

    @And("^([A-z0-9]+) is greater than or equal to (-?[0-9\\.]+)$")
    public void greaterThanOrEqualNumber(String field, String value){
        state.addConstraint(field, AtomicConstraintType.IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT.getText(), tryParse(value));
    }

    @And("^([A-z0-9]+) is less than (-?[0-9\\.]+)$")
    public void lessThanNumber(String field, String value){
        state.addConstraint(field, AtomicConstraintType.IS_LESS_THAN_CONSTANT.getText(), tryParse(value));
    }

    @And("^([A-z0-9]+) is less than or equal to (-?[0-9\\.]+)$")
    public void lessThanOrEqualNumber(String field, String value){
        state.addConstraint(field, AtomicConstraintType.IS_LESS_THAN_OR_EQUAL_TO_CONSTANT.getText(), tryParse(value));
    }

    @And("^([A-z0-9]+) is granular to ([0-9\\.]+)$")
    public void granularToNumber(String field, String value){
        state.addConstraint(field, AtomicConstraintType.IS_GRANULAR_TO.getText(), tryParse(value));
    }

    @And("^([A-z0-9]+) is anything but equal to (-?[0-9\\.]+)$")
    public void notEqualToNumber(String field, String value){
        state.addNotConstraint(field, AtomicConstraintType.IS_EQUAL_TO_CONSTANT.getText(), tryParse(value));
    }

    @And("^([A-z0-9]+) is anything but greater than (-?[0-9\\.]+)$")
    public void notGreaterThanNumber(String field, String value){
        state.addNotConstraint(field, AtomicConstraintType.IS_GREATER_THAN_CONSTANT.getText(), tryParse(value));
    }

    @And("^([A-z0-9]+) is anything but greater than or equal to (-?[0-9\\.]+)$")
    public void notGreaterThanOrEqualNumber(String field, String value){
        state.addNotConstraint(field, AtomicConstraintType.IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT.getText(), tryParse(value));
    }

    @And("^([A-z0-9]+) is anything but less than (-?[0-9\\.]+)$")
    public void notLessThanNumber(String field, String value){
        state.addNotConstraint(field, AtomicConstraintType.IS_LESS_THAN_CONSTANT.getText(), tryParse(value));
    }

    @And("^([A-z0-9]+) is anything but less than or equal to (-?[0-9\\.]+)$")
    public void notLessThanOrEqualNumber(String field, String value){
        state.addNotConstraint(field, AtomicConstraintType.IS_LESS_THAN_OR_EQUAL_TO_CONSTANT.getText(), tryParse(value));
    }

    @And("^([A-z0-9]+) is anything but granular to ([0-9\\.]+)$")
    public void notGranularToNumber(String field, String value){
        state.addNotConstraint(field, AtomicConstraintType.IS_GRANULAR_TO.getText(), tryParse(value));
    }

    @And("^(.+) is greater than field ([A-z0-9]+)$")
    public void numericGreater(String field, String otherField){
        state.addRelationConstraint(field, ConstraintType.GREATER_THAN, otherField);
    }

    @And("^(.+) is less than field ([A-z0-9]+)$")
    public void numericLess(String field, String otherField){
        state.addRelationConstraint(field, ConstraintType.LESS_THAN, otherField);
    }

    @And("^(.+) is greater than or equal to field ([A-z0-9]+)$")
    public void numericGreaterEqual(String field, String otherField){
        state.addRelationConstraint(field, ConstraintType.GREATER_THAN_OR_EQUAL_TO, otherField);
    }

    @And("^(.+) is less than or equal to field ([A-z0-9]+)$")
    public void numericLessEqual(String field, String otherField){
        state.addRelationConstraint(field, ConstraintType.LESS_THAN_OR_EQUAL_TO, otherField);
    }

    @Then("{fieldVar} contains numeric data")
    public void producedDataShouldContainNumericValuesForField(String fieldName){
        helper.assertFieldContainsSomeOf(fieldName, Number.class);
    }

    @Then("{fieldVar} contains only numeric data")
    public void producedDataShouldContainOnlyNumericValuesForField(String fieldName){
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
