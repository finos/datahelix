package com.scottlogic.deg.generator.cucumber.testframework.utils;

import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.reporters.ProfileValidationReporter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/** Collects all validation alerts it receives, and exposes them through a getter. Useful for testing purposes. */
public class RecordingProfileValidationReporter implements ProfileValidationReporter {
    private final Collection<ValidationAlert> recordedAlerts = new ArrayList<>();

    @Override
    public void output(Collection<ValidationAlert> alerts) {
        recordedAlerts.addAll(alerts);
    }

    Collection<ValidationAlert> getRecordedAlerts() {
        return Collections.unmodifiableCollection(this.recordedAlerts);
    }
}
