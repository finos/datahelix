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
import com.scottlogic.datahelix.generator.orchestrator.cucumber.testframework.utils.GeneratorTestUtilities;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.ConstraintType;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.stream.Collectors;

public class SetValueStep {
    private final CucumberTestState state;

    public SetValueStep(CucumberTestState state) {
        this.state = state;
    }

    @When("^([A-z0-9]+) is in set:")
    public void whenFieldIsConstrainedBySetValue(String fieldName, List<String> values) {
        final List<Object> parsedInput = values.stream()
            .map(this::parseInputSafely)
            .collect(Collectors.toList());

        this.state.addConstraint(fieldName, ConstraintType.IN_SET, parsedInput);
    }

    @When("^([A-z0-9]+) is anything but in set:")
    public void whenFieldIsNotConstrainedBySetValue(String fieldName, List<String> values) {
        final List<Object> parsedInput = values.stream()
            .map(this::parseInputSafely)
            .collect(Collectors.toList());

        this.state.addNotConstraint(fieldName, ConstraintType.IN_SET, parsedInput);
    }

    private Object parseInputSafely(String value) {
        try {
            return GeneratorTestUtilities.parseInput(value);
        } catch (JsonParseException e) {
            state.addException(e);
            return value;
        }
    }
}
