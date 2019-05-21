package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.ValidationType;
import com.scottlogic.deg.generator.inputs.validation.messages.StandardValidationMessages;
import com.scottlogic.deg.generator.inputs.validation.messages.StringValidationMessage;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A style of ValidationRule that can be created from simple arrow functions; removes the boilerplate of building up
 * collections of alerts.
 */
class ConsumerBasedValidationRule implements ValidationRule {
    private final Implementation implementation;

    ConsumerBasedValidationRule(Implementation implementation) {
        this.implementation = implementation;
    }

    @Override
    public Collection<ValidationAlert> performValidation() {
        Collection<ValidationAlert> alerts = new ArrayList<>();

        this.implementation.raiseErrors(
            new ValidationResultsImpl(alerts));

        return alerts;
    }

    private class ValidationResultsImpl implements Implementation.ValidationResults {
        private final Collection<ValidationAlert> alerts;

        private ValidationResultsImpl(Collection<ValidationAlert> alerts) {
            this.alerts = alerts;
        }

        @Override
        public void add(
            Criticality criticality,
            StandardValidationMessages message,
            ValidationType validationType,
            Field field) {

            alerts.add(new ValidationAlert(criticality, message, validationType, field));
        }
    }

    interface Implementation {
        void raiseErrors(ValidationResults results);

        interface ValidationResults {
            void add(
                Criticality criticality,
                StandardValidationMessages message,
                ValidationType validationType,
                Field field);

            default void addError(
                String message,
                ValidationType validationType) {

                add(
                    Criticality.ERROR,
                    new StringValidationMessage(message),
                    validationType,
                    null);
            }

            default void addInputError(String format, Object... args) {
                add(
                    Criticality.ERROR,
                    new StringValidationMessage(
                        String.format(format, args)),
                    ValidationType.INPUT,
                    null);
            }
        }
    }
}
