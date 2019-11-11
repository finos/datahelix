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

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import org.junit.AssumptionViolatedException;

public class CucumberHooks {
    @Before("@ignore")
    public void beforeEachScenario(Scenario scenario) {
        throw new AssumptionViolatedException(String.format("Scenario '%s' is ignored (%s)", scenario.getName(), scenario.getId()));
    }
}

