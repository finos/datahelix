package com.scottlogic.deg.generator.inputs.validation;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.inputs.validation.reporters.ProfileValidationReporter;
import com.scottlogic.deg.generator.inputs.validation.reporters.SystemOutProfileValidationReporter;

public class SystemOutProfileValidator implements ProfileValidator {

    private ProfileValidationVisitor visitor;
    private ProfileValidationReporter reporter;

    public SystemOutProfileValidator(){
        this.visitor = new ProfileValidationVisitor();
        this.reporter = new SystemOutProfileValidationReporter();
    }

    @Override
    public void validate(Profile profile) {
        profile.accept(visitor);
        reporter.output(visitor.getAlerts());
    }
}
