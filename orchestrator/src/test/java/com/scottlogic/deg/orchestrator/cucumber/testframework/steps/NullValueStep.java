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
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.Objects;

public class NullValueStep {

    private final CucumberTestState state;
    private final CucumberTestHelper helper;

    public NullValueStep(CucumberTestState state, CucumberTestHelper helper){
        this.state = state;
        this.helper = helper;
    }

    @When("{fieldVar} is equal to null")
    public void whenFieldIsConstrainedByTextValue(String fieldName) {
        state.addConstraint(fieldName, ConstraintType.EQUAL_TO, null);
    }

    @When("{fieldVar} is anything but {operator} null")
    public void whenFieldIsNotConstrainedByTextValue(String fieldName, String constraintName) {
        state.addNotConstraint(fieldName, ConstraintType.fromPropertyName(constraintName), null);
    }

    @Then("{fieldVar} contains anything but null")
    public void producedDataShouldNotContainNull(String fieldName) {
        helper.assertFieldContainsOnly(fieldName, Objects::nonNull);
    }
}
