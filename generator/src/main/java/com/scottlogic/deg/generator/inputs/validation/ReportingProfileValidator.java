package com.scottlogic.deg.generator.inputs.validation;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.inputs.validation.reporters.ProfileValidationReporter;

import java.util.Collection;

public class ReportingProfileValidator implements ProfileValidator {
    @Override
    public Collection<ValidationAlert> validate(Profile profile) {
        ProfileValidationVisitor visitor = new ProfileValidationVisitor();

        profile.accept(visitor);
        return visitor.getAlerts();
    }
}
