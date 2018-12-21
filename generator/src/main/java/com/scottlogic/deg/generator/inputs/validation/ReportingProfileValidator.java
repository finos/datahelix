package com.scottlogic.deg.generator.inputs.validation;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.inputs.validation.reporters.ProfileValidationReporter;
import com.scottlogic.deg.generator.inputs.validation.reporters.SystemOutProfileValidationReporter;

public class ReportingProfileValidator implements ProfileValidator {

    private final ProfileValidationReporter reporter;

    public ReportingProfileValidator(ProfileValidationReporter reporter){
        this.reporter = reporter;
    }

    @Override
    public void validate(Profile profile) {
        ProfileValidationVisitor visitor = new ProfileValidationVisitor();

        profile.accept(visitor);
        this.reporter.output(visitor.getAlerts());
    }
}
