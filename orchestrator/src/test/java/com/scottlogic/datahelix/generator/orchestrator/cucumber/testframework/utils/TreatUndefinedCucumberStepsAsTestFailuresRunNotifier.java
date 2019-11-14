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

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;

public class TreatUndefinedCucumberStepsAsTestFailuresRunNotifier extends RunNotifier {
    private final RunNotifier notifier;

    public TreatUndefinedCucumberStepsAsTestFailuresRunNotifier(RunNotifier notifier) {
        this.notifier = notifier;
    }

    @Override
    public void fireTestAssumptionFailed(Failure failure) {
        //NOTE: This change treats undefined steps as failures rather than undefined behaviour
        this.fireTestFailure(failure);
    }

    @Override
    public void addListener(RunListener listener) {
        notifier.addListener(listener);
    }

    @Override
    public void removeListener(RunListener listener) {
        notifier.removeListener(listener);
    }

    @Override
    public void fireTestRunStarted(Description description) {
        notifier.fireTestRunStarted(description);
    }

    @Override
    public void fireTestRunFinished(Result result) {
        notifier.fireTestRunFinished(result);
    }

    @Override
    public void fireTestStarted(Description description) throws StoppedByUserException {
        notifier.fireTestStarted(description);
    }

    @Override
    public void fireTestFailure(Failure failure) {
        notifier.fireTestFailure(failure);
    }

    @Override
    public void fireTestIgnored(Description description) {
        notifier.fireTestIgnored(description);
    }

    @Override
    public void fireTestFinished(Description description) {
        notifier.fireTestFinished(description);
    }

    @Override
    public void pleaseStop() {
        notifier.pleaseStop();
    }

    @Override
    public void addFirstListener(RunListener listener) {
        notifier.addFirstListener(listener);
    }
}
