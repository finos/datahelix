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

import com.scottlogic.deg.common.profile.constraintdetail.AtomicConstraintType;
import com.scottlogic.deg.generator.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.orchestrator.cucumber.testframework.utils.CucumberTestHelper;
import com.scottlogic.deg.orchestrator.cucumber.testframework.utils.CucumberTestState;
import com.scottlogic.deg.orchestrator.cucumber.testframework.utils.GeneratorTestUtilities;
import com.scottlogic.deg.profile.common.ConstraintType;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.time.OffsetDateTime;
import java.util.function.Function;

public class DateValueStep {

    public static final String DATE_REGEX = "(-?(\\d{4,19})-(\\d{2})-(\\d{2}T(\\d{2}:\\d{2}:\\d{2}\\.\\d{3}))Z?)";
    private final CucumberTestState state;
    private final CucumberTestHelper helper;

    public DateValueStep(CucumberTestState state, CucumberTestHelper helper){
        this.state = state;
        this.helper = helper;
    }

    @When("^([A-z0-9]+) is equal to ([0-9]{4,5}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z?)$")
    public void equalToDateValue(String fieldName, String value) {
        state.addConstraint(fieldName, AtomicConstraintType.IS_EQUAL_TO_CONSTANT.getText(), value);
    }

    @When("^([A-z0-9]+) is after ([0-9]{4,5}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z?)$")
    public void afterDateValue(String fieldName, String value) {
        state.addConstraint(fieldName, AtomicConstraintType.IS_AFTER_CONSTANT_DATE_TIME.getText(), value);
    }

    @When("^([A-z0-9]+) is after or at ([0-9]{4,5}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z?)$")
    public void afterOrAtDateValue(String fieldName, String value) {
        state.addConstraint(fieldName, AtomicConstraintType.IS_AFTER_OR_EQUAL_TO_CONSTANT_DATE_TIME.getText(), value);
    }

    @When("^([A-z0-9]+) is before ([0-9]{4,5}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z?)$")
    public void beforeDateValue(String fieldName, String value) {
        state.addConstraint(fieldName, AtomicConstraintType.IS_BEFORE_CONSTANT_DATE_TIME.getText(), value);
    }

    @When("^([A-z0-9]+) is before or at ([0-9]{4,5}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z?)$")
    public void beforeOrAtDateValue(String fieldName, String value) {
        state.addConstraint(fieldName, AtomicConstraintType.IS_BEFORE_OR_EQUAL_TO_CONSTANT_DATE_TIME.getText(), value);
    }

    @When("^([A-z0-9]+) is granular to \"(.*)\"$")
    public void granularToDateValue(String fieldName, String value) {
        state.addConstraint(fieldName, AtomicConstraintType.IS_GRANULAR_TO.getText(), value);
    }

    @When("^([A-z0-9]+) is anything but equal to ([0-9]{4,5}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z?)$")
    public void notEqualToDateValue(String fieldName, String value) {
        state.addNotConstraint(fieldName, AtomicConstraintType.IS_EQUAL_TO_CONSTANT.getText(), value);
    }

    @When("^([A-z0-9]+) is anything but after ([0-9]{4,5}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z?)$")
    public void notAfterDateValue(String fieldName, String value) {
        state.addNotConstraint(fieldName, AtomicConstraintType.IS_AFTER_CONSTANT_DATE_TIME.getText(), value);
    }

    @When("^([A-z0-9]+) is anything but after or at ([0-9]{4,5}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z?)$")
    public void notAfterOrAtDateValue(String fieldName, String value) {
        state.addNotConstraint(fieldName, AtomicConstraintType.IS_AFTER_OR_EQUAL_TO_CONSTANT_DATE_TIME.getText(), value);
    }

    @When("^([A-z0-9]+) is anything but before ([0-9]{4,5}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z?)$")
    public void notBeforeDateValue(String fieldName, String value) {
        state.addNotConstraint(fieldName, AtomicConstraintType.IS_BEFORE_CONSTANT_DATE_TIME.getText(), value);
    }

    @When("^([A-z0-9]+) is anything but before or at ([0-9]{4,5}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z?)$")
    public void notBeforeOrAtDateValue(String fieldName, String value) {
        state.addNotConstraint(fieldName, AtomicConstraintType.IS_BEFORE_OR_EQUAL_TO_CONSTANT_DATE_TIME.getText(), value);
    }

    @When("^([A-z0-9]+) is anything but granular to \"(.*)\"$")
    public void notGranularToDateValue(String fieldName, String value) {
        state.addNotConstraint(fieldName, AtomicConstraintType.IS_GRANULAR_TO.getText(), value);
    }

    @And("^(.+) is after field ([A-z0-9]+)$")
    public void dateAfter(String field, String otherField){
        state.addRelationConstraint(field, ConstraintType.AFTER, otherField);
    }

    @And("^(.+) is after or at field ([A-z0-9]+)$")
    public void dateAfterOrAt(String field, String otherField){
        state.addRelationConstraint(field, ConstraintType.AFTER_OR_AT, otherField);
    }

    @And("^(.+) is before field ([A-z0-9]+)$")
    public void dateBefore(String field, String otherField){
        state.addRelationConstraint(field, ConstraintType.BEFORE, otherField);
    }

    @And("^(.+) is before or at field ([A-z0-9]+)$")
    public void dateBeforeOrAt(String field, String otherField){
        state.addRelationConstraint(field, ConstraintType.BEFORE_OR_AT, otherField);
    }

    @Then("{fieldVar} contains only datetime data")
    public void producedDataShouldContainOnlyDateTimeValuesForField(String fieldName){
        helper.assertFieldContainsNullOrMatching(fieldName, OffsetDateTime.class);
    }

    @Then("{fieldVar} contains datetime data")
    public void producedDataShouldContainStringValuesForField(String fieldName){
        helper.assertFieldContainsSomeOf(fieldName, OffsetDateTime.class);
    }

    @Then("{fieldVar} contains anything but datetime data")
    public void producedDataShouldContainAnythingButStringValuesForField(String fieldName){
        helper.assertFieldContainsNullOrNotMatching(fieldName, OffsetDateTime.class);
    }

    @Then("{fieldVar} contains datetimes between {date} and {date} inclusively")
    public void producedDataShouldContainDateTimeValuesInRangeForField(String fieldName,  String minInclusive, String maxInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            OffsetDateTime.class,
            isBetweenInclusively(minInclusive, maxInclusive));
    }

    @Then("{fieldVar} contains datetimes outside {date} and {date}")
    public void producedDataShouldContainDateTimeValuesOutOfRangeForField(String fieldName, String min, String max){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            OffsetDateTime.class,
            value -> !isBetweenInclusively(min, max).apply(value));
    }

    @Then("{fieldVar} contains datetimes before or at {date}")
    public void producedDataShouldContainDateTimeValuesBeforeForField(String fieldName, String beforeInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            OffsetDateTime.class,
            value -> isBeforeOrAt(value, beforeInclusive));
    }

    @Then("{fieldVar} contains datetimes after or at {date}")
    public void producedDataShouldContainDateTimeValuesAfterForField(String fieldName, String afterInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            OffsetDateTime.class,
            value -> isAfterOrAt(value, afterInclusive));
    }

    private Function<OffsetDateTime, Boolean> isBetweenInclusively(String minInclusive, String maxInclusive){
        return value -> isAfterOrAt(value, minInclusive) && isBeforeOrAt(value, maxInclusive);
    }

    private boolean isAfterOrAt(OffsetDateTime date, String minInclusiveString){
        OffsetDateTime minInclusive = getDateTime(minInclusiveString);
        return date.equals(minInclusive) || date.isAfter(minInclusive);
    }

    private boolean isBeforeOrAt(OffsetDateTime date, String maxInclusiveString){
        OffsetDateTime maxInclusive = getDateTime(maxInclusiveString);
        return date.equals(maxInclusive) || date.isBefore(maxInclusive);
    }
    private OffsetDateTime getDateTime(String date){
        return GeneratorTestUtilities.getOffsetDateTime(date);
    }
}

