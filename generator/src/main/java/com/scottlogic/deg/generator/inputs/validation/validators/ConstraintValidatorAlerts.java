package com.scottlogic.deg.generator.inputs.validation.validators;

import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;

import java.util.List;

public interface ConstraintValidatorAlerts {

    List<ValidationAlert> getAlerts();
}
