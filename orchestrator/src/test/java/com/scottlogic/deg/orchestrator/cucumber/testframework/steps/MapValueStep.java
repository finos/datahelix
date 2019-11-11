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
import cucumber.api.java.en.And;

import java.util.List;

public class MapValueStep {

    private final CucumberTestState state;

    public MapValueStep(CucumberTestState state) {
        this.state = state;
    }

    @And("the file \"(.+)\" contains the following data:")
    public void whenMapDataStructure(String name, List<List<String>> values) {
        state.addMapFile(name, values);
    }

    @And("^(.+) is from (.+) in (.+)$")
    public void whenFieldIsInMap(String field, String key, String map) {
        state.addInMapConstraint(field, key, map);
    }
}
