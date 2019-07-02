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
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.function.Function;

public class StringValueStep {

    private final CucumberTestState state;
    private final CucumberTestHelper helper;

    public StringValueStep(CucumberTestState state, CucumberTestHelper helper){
        this.state = state;
        this.helper = helper;
    }

    @When("{fieldVar} is {operator} {string}")
    public void whenFieldIsConstrainedByTextValue(String fieldName, String constraintName, String value) throws Exception {
        state.addConstraint(fieldName, constraintName, value);
    }

    @When("{fieldVar} is anything but {operator} {string}")
    public void whenFieldIsNotConstrainedByTextValue(String fieldName, String constraintName, String value) throws Exception {
        state.addNotConstraint(fieldName, constraintName, value);
    }

    @Then("{fieldVar} contains only string data")
    public void producedDataShouldContainOnlyStringValuesForField(String fieldName){
        helper.assertFieldContainsNullOrMatching(fieldName, String.class);
    }

    @Then("{fieldVar} contains string data")
    public void producedDataShouldContainStringValuesForField(String fieldName){
        helper.assertFieldContainsSomeOf(fieldName, String.class);
    }

    @Then("{fieldVar} contains anything but string data")
    public void producedDataShouldContainAnythingButStringValuesForField(String fieldName){
        helper.assertFieldContainsNullOrNotMatching(fieldName, String.class);
    }

    @Then("{fieldVar} contains strings of length between {int} and {int} inclusively")
    public void producedDataShouldContainStringValuesInRangeForField(String fieldName, int minInclusive, int maxInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            String.class,
            isLengthBetweenInclusively(minInclusive, maxInclusive));
    }

    @Then("{fieldVar} contains strings shorter than or equal to {int}")
    public void producedDataShouldContainStringValuesShorterThanForField(String fieldName, int shorterThanInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            String.class,
            value -> isShorterThanOrEqual(value, shorterThanInclusive));
    }

    @Then("{fieldVar} contains strings longer than or equal to {int}")
    public void producedDataShouldContainStringValuesLongerThanForField(String fieldName, int longerThanInclusive){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            String.class,
            value -> isLongerThanOrEqual(value, longerThanInclusive));
    }

    @Then("{fieldVar} contains strings of length outside {int} and {int}")
    public void producedDataShouldContainStringValuesOutOfRangeForField(String fieldName, int min, int max){
        helper.assertFieldContainsNullOrMatching(
            fieldName,
            String.class,
            value -> !isLengthBetweenInclusively(min, max).apply(value));
    }

    private Function<String, Boolean> isLengthBetweenInclusively(int minInclusive, int maxInclusive){
        return value -> isLongerThanOrEqual(value, minInclusive) && isShorterThanOrEqual(value, maxInclusive);
    }

    private boolean isShorterThanOrEqual(String value, int maxLengthInclusive) {
        return value.length() <= maxLengthInclusive;
    }

    private boolean isLongerThanOrEqual(String value, int minLengthInclusive) {
        return value.length() >= minLengthInclusive;
    }
}
