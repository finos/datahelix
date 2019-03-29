package com.scottlogic.deg.generator.cucumber.testframework.utils;

import cucumber.api.junit.Cucumber;
import cucumber.runtime.junit.FeatureRunner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

public class GeneratorCucumber extends Cucumber {

    private RunNotifier notifier;

    /**
     * Constructor called by JUnit.
     *
     * @param clazz the class with the @RunWith annotation.
     * @throws InitializationError if there is another problem
     */
    public GeneratorCucumber(Class clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    protected void runChild(FeatureRunner child, RunNotifier notifier) {
        if (this.notifier == null)
            this.notifier = new TreatUndefinedCucumberStepsAsTestFailuresRunNotifier(notifier); //NOTE: Not thread safe

        super.runChild(child, this.notifier);
    }
}
