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

public class BooleanValueStep {
    private final CucumberTestState state;

    public BooleanValueStep(CucumberTestState state) {
        this.state = state;
    }

    @When("{fieldVar} is equal to {boolean}")
    public void whenFieldIsConstrainedByNumericValue(String fieldName, Boolean value) {
        this.state.addConstraint(fieldName, ConstraintType.EQUAL_TO, value);
    }

    @When("{fieldVar} is anything but {operator} {boolean}")
    public void whenFieldIsNotConstrainedByNumericValue(String fieldName, String constraintName, Boolean value) {
        this.state.addNotConstraint(fieldName, ConstraintType.fromPropertyName(constraintName), value);
    }
}
