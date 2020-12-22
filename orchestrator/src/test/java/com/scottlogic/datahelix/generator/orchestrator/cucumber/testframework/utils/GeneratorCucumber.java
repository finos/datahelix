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

package com.scottlogic.datahelix.generator.orchestrator.cucumber.testframework.utils;

import io.cucumber.junit.Cucumber;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

public class GeneratorCucumber extends Runner {
    private RunNotifier notifier;
    private Class clazz;
    private Cucumber cucumber;

    /**
     * Constructor called by JUnit.
     *
     * @param clazzValue the class with the @RunWith annotation.
     * @throws InitializationError if there is another problem
     */
    public GeneratorCucumber(Class clazzValue) throws InitializationError {
        clazz = clazzValue;
        cucumber = new Cucumber(clazzValue);
    }

    @Override
    public Description getDescription() {
        return cucumber.getDescription();
    }

    @Override
    public void run(RunNotifier notifier) {
        if (this.notifier == null) {
            this.notifier = new TreatUndefinedCucumberStepsAsTestFailuresRunNotifier(notifier); //NOTE: Not thread safe
        }

        cucumber.run(this.notifier);
    }
}
