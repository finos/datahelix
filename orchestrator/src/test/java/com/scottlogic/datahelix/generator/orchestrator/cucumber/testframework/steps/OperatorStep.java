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

import com.scottlogic.datahelix.generator.profile.dtos.constraints.ConstraintType;
import io.cucumber.java8.En;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OperatorStep implements En {

    public OperatorStep() {
        Set<ConstraintType> allOperators = new HashSet<>(Arrays.asList(ConstraintType.values()));
        String regex = this.getHumanReadableOperationRegex(allOperators);

        ParameterType("operator", regex, this::extractConstraint);
    }

    private String getHumanReadableOperationRegex(Set<ConstraintType> types) {
        return
            types.stream()
                .map(act -> act.propertyName.replaceAll("([a-z])([A-Z]+)", "$1 $2").toLowerCase())
                .collect(Collectors.joining("|", "(", ")"));
    }

    private String extractConstraint(String gherkinConstraint) {
        List<String> allConstraints = Arrays.asList(gherkinConstraint.split(" "));
        return allConstraints.get(0) + allConstraints
            .stream()
            .skip(1)
            .map(value -> value.substring(0, 1).toUpperCase() + value.substring(1))
            .collect(Collectors.joining());
    }
}
