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
import com.scottlogic.deg.profile.dtos.constraints.ConstraintType;
import cucumber.api.java.en.When;

public class TimeValueStep {
    public static final String TIME_REGEX = "([0-9]{2}:[0-9]{2}:[0-9]{2}\\.[0-9]{3})";
    private final CucumberTestState state;
    private final CucumberTestHelper helper;

    public TimeValueStep(CucumberTestState state, CucumberTestHelper helper) {
        this.state = state;
        this.helper = helper;
    }

    @When("^([A-z0-9]+) is equal to " + TIME_REGEX)
    public void equalToTimeValue(String fieldName, String value) {
        state.addConstraint(fieldName, ConstraintType.EQUAL_TO, value);
    }

    @When("^([A-z0-9]+) is after " + TIME_REGEX)
    public void afterTimeValue(String fieldName, String value) {
        state.addConstraint(fieldName, ConstraintType.AFTER, value);
    }

    @When("^([A-z0-9]+) is after or at " + TIME_REGEX)
    public void afterOrAtTimeValue(String fieldName, String value) {
        state.addConstraint(fieldName, ConstraintType.AFTER_OR_AT, value);
    }

    @When("^([A-z0-9]+) is before " + TIME_REGEX)
    public void beforeTimeValue(String fieldName, String value) {
        state.addConstraint(fieldName, ConstraintType.BEFORE, value);
    }

    @When("^([A-z0-9]+) is before or at " + TIME_REGEX)
    public void beforeOrAtTimeValue(String fieldName, String value) {
        state.addConstraint(fieldName, ConstraintType.BEFORE_OR_AT, value);
    }

    @When("^([A-z0-9]+) is anything but equal to " + TIME_REGEX)
    public void notEqualToTimeValue(String fieldName, String value) {
        state.addNotConstraint(fieldName, ConstraintType.EQUAL_TO, value);
    }

    @When("^([A-z0-9]+) is anything but after " + TIME_REGEX)
    public void notAfterTimeValue(String fieldName, String value) {
        state.addNotConstraint(fieldName, ConstraintType.AFTER, value);
    }

    @When("^([A-z0-9]+) is anything but after or at " + TIME_REGEX)
    public void notAfterOrAtTimeValue(String fieldName, String value) {
        state.addNotConstraint(fieldName, ConstraintType.AFTER_OR_AT, value);
    }

    @When("^([A-z0-9]+) is anything but before " + TIME_REGEX)
    public void notBeforeDateTimeValue(String fieldName, String value) {
        state.addNotConstraint(fieldName, ConstraintType.BEFORE, value);
    }

    @When("^([A-z0-9]+) is anything but before or at " + TIME_REGEX)
    public void notBeforeOrAtTimeValue(String fieldName, String value) {
        state.addNotConstraint(fieldName, ConstraintType.BEFORE_OR_AT, value);
    }
}
