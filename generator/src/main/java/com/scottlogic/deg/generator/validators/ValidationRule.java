package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;

import java.util.Collection;
import java.util.Collections;

public interface ValidationRule {
    Collection<ValidationAlert> performValidation();

    default ValidationRule onlyIf(boolean conditionIsTrue) {
        return conditionIsTrue
            ? this
            : Collections::emptySet;
    }

    default ValidationRule unless(boolean conditionIsTrue) {
        return onlyIf(!conditionIsTrue);
    }
}
