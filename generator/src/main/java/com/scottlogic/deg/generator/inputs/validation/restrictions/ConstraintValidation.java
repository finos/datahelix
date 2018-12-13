package com.scottlogic.deg.generator.inputs.validation.restrictions;

import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;

import java.util.List;

public interface ConstraintValidation {

    List<ValidationAlert> getAlerts();
}
