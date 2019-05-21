package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.reporters.ProfileValidationReporter;

import java.util.Collection;

/**
 * Given a set of rules, applies each of them and issues the result to reporters, throwing if a terminal exception
 * occurs
 */
public class Validator {
    private final ProfileValidationReporter validationReporter;

    @Inject
    Validator(ProfileValidationReporter validationReporter) {
        this.validationReporter = validationReporter;
    }

    public void validateAll(ValidationRule... validationRules) {
        for (ValidationRule rule : validationRules) {
            Collection<ValidationAlert> alerts = rule.performValidation();

            if (!alerts.isEmpty()) {
                validationReporter.output(alerts);

                if (validationResultShouldHaltExecution(alerts)) {
                    throw new ValidationException("Validation errors occurred");
                }
            }
        }
    }

    private static boolean validationResultShouldHaltExecution(Collection<ValidationAlert> alerts) {
        return alerts.stream()
            .anyMatch(alert ->
                alert.getCriticality().equals(Criticality.ERROR));
    }
}
