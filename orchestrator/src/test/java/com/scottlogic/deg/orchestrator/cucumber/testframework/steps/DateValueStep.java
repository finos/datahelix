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

import com.scottlogic.deg.orchestrator.cucumber.testframework.utils.CucumberTestState;
import com.scottlogic.deg.profile.common.ConstraintType;
import cucumber.api.java.en.When;

public class DateValueStep {
    private final CucumberTestState state;
    public static final String DATE_REGEX = "(-?(\\d{4,19})-(\\d{2})-(\\d{2}))";

    public DateValueStep (CucumberTestState state){
        this.state = state;
    }

    @When("^([A-z0-9]+) is equal to ([0-9]{4,5}-[0-9]{2}-[0-9]{2})$")
    public void equalToDateValue(String fieldName, String value) {
        state.addConstraint(fieldName, ConstraintType.EQUAL_TO, value);
    }

    @When("^([A-z0-9]+) is after ([0-9]{4,5}-[0-9]{2}-[0-9]{2})$")
    public void afterDateValue(String fieldName, String value) {
        state.addConstraint(fieldName, ConstraintType.AFTER, value);
    }

    @When("^([A-z0-9]+) is after or at ([0-9]{4,5}-[0-9]{2}-[0-9]{2})$")
    public void afterOrAtDateValue(String fieldName, String value) {
        state.addConstraint(fieldName, ConstraintType.AFTER_OR_AT, value);
    }

    @When("^([A-z0-9]+) is before ([0-9]{4,5}-[0-9]{2}-[0-9]{2})$")
    public void beforeDateValue(String fieldName, String value) {
        state.addConstraint(fieldName, ConstraintType.BEFORE, value);
    }

    @When("^([A-z0-9]+) is before or at ([0-9]{4,5}-[0-9]{2}-[0-9]{2})$")
    public void beforeOrAtDateValue(String fieldName, String value) {
        state.addConstraint(fieldName, ConstraintType.BEFORE_OR_AT, value);
    }

    @When("^([A-z0-9]+) is anything but equal to ([0-9]{4,5}-[0-9]{2}-[0-9]{2})$")
    public void notEqualToDateValue(String fieldName, String value) {
        state.addNotConstraint(fieldName, ConstraintType.EQUAL_TO, value);
    }

    @When("^([A-z0-9]+) is anything but after ([0-9]{4,5}-[0-9]{2}-[0-9]{2})$")
    public void notAfterDateValue(String fieldName, String value) {
        state.addNotConstraint(fieldName, ConstraintType.AFTER, value);
    }

    @When("^([A-z0-9]+) is anything but after or at ([0-9]{4,5}-[0-9]{2}-[0-9]{2})$")
    public void notAfterOrAtDateValue(String fieldName, String value) {
        state.addNotConstraint(fieldName, ConstraintType.AFTER_OR_AT, value);
    }

    @When("^([A-z0-9]+) is anything but before ([0-9]{4,5}-[0-9]{2}-[0-9]{2})$")
    public void notBeforeDateValue(String fieldName, String value) {
        state.addNotConstraint(fieldName, ConstraintType.BEFORE, value);
    }

    @When("^([A-z0-9]+) is anything but before or at ([0-9]{4,5}-[0-9]{2}-[0-9]{2})$")
    public void notBeforeOrAtDateValue(String fieldName, String value) {
        state.addNotConstraint(fieldName, ConstraintType.BEFORE_OR_AT, value);
    }
}


