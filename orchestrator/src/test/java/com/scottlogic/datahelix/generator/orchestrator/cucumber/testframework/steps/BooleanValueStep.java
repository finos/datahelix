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

import com.fasterxml.jackson.core.JsonParseException;
import com.scottlogic.datahelix.generator.orchestrator.cucumber.testframework.utils.CucumberTestState;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.ConstraintType;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.When;

public class BooleanValueStep {
    private final CucumberTestState state;

    public BooleanValueStep(CucumberTestState state) {
        this.state = state;
    }

    @ParameterType(name = "boolean", value = "(true|false)$")
    public Boolean defineBoolean(String value) throws JsonParseException {
        return Boolean.valueOf(value);
    }

    @When("{fieldVar} is equal to {boolean}")
    public void whenFieldIsConstrainedByNumericValue(String fieldName, Boolean value) {
        this.state.addConstraint(fieldName, ConstraintType.EQUAL_TO, value);
    }

    @When("{fieldVar} is anything but {operator} {boolean}")
    public void whenFieldIsNotConstrainedByNumericValue(String fieldName, String constraintName, Boolean value) {
        this.state.addNotConstraint(fieldName, ConstraintType.fromName(constraintName), value);
    }
}
