package com.scottlogic.deg.generator.cucumber.testframework.utils;

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
