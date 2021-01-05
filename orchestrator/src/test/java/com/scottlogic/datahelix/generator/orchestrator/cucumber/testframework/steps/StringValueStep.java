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

package com.scottlogic.datahelix.generator.orchestrator.cucumber.testframework.steps;

import com.scottlogic.datahelix.generator.orchestrator.cucumber.testframework.utils.CucumberTestHelper;
import com.scottlogic.datahelix.generator.orchestrator.cucumber.testframework.utils.CucumberTestState;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.ConstraintType;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.function.Function;

import static java.lang.Integer.parseInt;

public class StringValueStep {
    private final CucumberTestState state;
    private final CucumberTestHelper helper;

    public StringValueStep(CucumberTestState state, CucumberTestHelper helper) {
        this.state = state;
        this.helper = helper;
    }

    @When("^([A-z0-9]+) is equal to \"(.*)\"$")
    public void equalToString(String fieldName, String value) {
        state.addConstraint(fieldName, ConstraintType.EQUAL_TO, value);
    }

    @When("^([A-z0-9]+) is anything but equal to \"(.*)\"$")
    public void notEqualToString(String fieldName, String value) {
        state.addNotConstraint(fieldName, ConstraintType.EQUAL_TO, value);
    }

    @When("^([A-z0-9]+) is shorter than (-?[0-9\\.]+)$")
    public void shorterThanString(String fieldName, String value) {
        state.addConstraint(fieldName, ConstraintType.SHORTER_THAN, parseInt(value));
    }

    @When("^([A-z0-9]+) is anything but shorter than (-?[0-9\\.]+)$")
    public void notShorterThanString(String fieldName, String value) {
        state.addNotConstraint(fieldName, ConstraintType.SHORTER_THAN, parseInt(value));
    }

    @When("^([A-z0-9]+) is longer than (-?[0-9\\.]+)$")
    public void longerThanString(String fieldName, String value) {
        state.addConstraint(fieldName, ConstraintType.LONGER_THAN, parseInt(value));
    }

    @When("^([A-z0-9]+) is anything but longer than (-?[0-9\\.]+)$")
    public void notLongerThanString(String fieldName, String value) {
        state.addNotConstraint(fieldName, ConstraintType.LONGER_THAN, parseInt(value));
    }

    @When("^([A-z0-9]+) is of length (-?[0-9\\.]+)$")
    public void ofLengthString(String fieldName, String value) {
        state.addConstraint(fieldName, ConstraintType.OF_LENGTH, parseInt(value));
    }

    @When("^([A-z0-9]+) is anything but of length (-?[0-9\\.]+)$")
    public void notOfLengthString(String fieldName, String value) {
        state.addNotConstraint(fieldName, ConstraintType.OF_LENGTH, parseInt(value));
    }

    @Then("{fieldVar} contains only string data")
    public void producedDataShouldContainOnlyStringValuesForField(String fieldName) {
        helper.assertFieldContainsNullOrMatching(fieldName, String.class);
    }

    @Then("{fieldVar} contains string data")
    public void producedDataShouldContainStringValuesForField(String fieldName) {
        helper.assertFieldContainsSomeOf(fieldName, String.class);
    }

    @Then("{fieldVar} contains anything but string data")
    public void producedDataShouldContainAnythingButStringValuesForField(String fieldName) {
        helper.assertFieldContainsNullOrNotMatching(fieldName, String.class);
    }

    @Then("{fieldVar} contains strings of length between {int} and {int} inclusively")
    public void producedDataShouldContainStringValuesInRangeForField(String fieldName, int minInclusive, int maxInclusive) {
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            String.class,
            isLengthBetweenInclusively(minInclusive, maxInclusive));
    }

    @Then("{fieldVar} contains strings shorter than or equal to {int}")
    public void producedDataShouldContainStringValuesShorterThanForField(String fieldName, int shorterThanInclusive) {
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            String.class,
            value -> isShorterThanOrEqual(value, shorterThanInclusive));
    }

    @Then("{fieldVar} contains strings longer than or equal to {int}")
    public void producedDataShouldContainStringValuesLongerThanForField(String fieldName, int longerThanInclusive) {
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            String.class,
            value -> isLongerThanOrEqual(value, longerThanInclusive));
    }

    @Then("{fieldVar} contains strings of length outside {int} and {int}")
    public void producedDataShouldContainStringValuesOutOfRangeForField(String fieldName, int min, int max) {
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            String.class,
            value -> !isLengthBetweenInclusively(min, max).apply(value));
    }

    @When("^([A-z0-9]+) uses custom generator \"(.*)\"$")
    public void customGenerator(String fieldName, String value) {
        state.addConstraint(fieldName, ConstraintType.EQUAL_TO, value);
    }

    private Function<String, Boolean> isLengthBetweenInclusively(int minInclusive, int maxInclusive) {
        return value -> isLongerThanOrEqual(value, minInclusive) && isShorterThanOrEqual(value, maxInclusive);
    }

    private boolean isShorterThanOrEqual(String value, int maxLengthInclusive) {
        return value.length() <= maxLengthInclusive;
    }

    private boolean isLongerThanOrEqual(String value, int minLengthInclusive) {
        return value.length() >= minLengthInclusive;
    }
}
