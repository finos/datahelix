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

package com.scottlogic.deg.orchestrator.cucumber.testframework.utils;

import com.scottlogic.datahelix.generator.common.ValidationException;
import com.scottlogic.deg.generator.validators.ErrorReporter;

import java.util.stream.Collectors;

public class CucumberErrorReporter extends ErrorReporter {
    private final CucumberTestState state;

    public CucumberErrorReporter(CucumberTestState state) {
        this.state = state;
    }

    @Override
    public void displayException(Exception e) {
        state.addException(e);
    }

    @Override
    public void displayValidation(ValidationException e) {
        state.testExceptions.addAll(e.errorMessages.stream().map(ValidationException::new).collect(Collectors.toList()));
    }

}
