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

import com.scottlogic.datahelix.generator.core.config.detail.DataGenerationType;
import com.scottlogic.datahelix.generator.orchestrator.cucumber.testframework.utils.CucumberGenerationMode;
import io.cucumber.java.ParameterType;

import java.util.Arrays;

public class DataGenerationStep {
    @ParameterType(name = "generationStrategy", value = "(.*)$")
    public DataGenerationType defineGenerationStrategy(String value) {
        return Arrays.stream(DataGenerationType.values())
            .filter(val -> val.toString().equalsIgnoreCase(value))
            .findFirst().orElse(DataGenerationType.FULL_SEQUENTIAL);
    }

    @ParameterType(name = "generationMode", value = "(.*)$")
    public CucumberGenerationMode defineGenerationMode(String value) {
        return Arrays.stream(CucumberGenerationMode.values())
            .filter(val -> val.toString().equalsIgnoreCase(value))
            .findFirst().orElse(CucumberGenerationMode.VALIDATING);
    }
}
