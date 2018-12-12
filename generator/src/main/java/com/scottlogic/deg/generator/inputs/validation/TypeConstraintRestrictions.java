package com.scottlogic.deg.generator.inputs.validation;

import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

class TypeConstraintRestrictions implements ConstraintValidation {

    public final ValidationAlert.ValidationType ValidationType = ValidationAlert.ValidationType.TYPE;
    private HashSet<IsOfTypeConstraint.Types> allowedTypes;
    private List<ValidationAlert> alerts;

    public TypeConstraintRestrictions(){
        this.allowedTypes = new HashSet<>(Arrays.asList(IsOfTypeConstraint.Types.values()));
        this.alerts = new ArrayList<>();

    }

    public void IsOfType(String field, IsOfTypeConstraint.Types type) {

        if (this.allowedTypes.contains(type)) {
            this.allowedTypes = new HashSet<>(Arrays.asList(type));
        }
        else
        {
            logError(field, String.format("Type %s is not allowed for this field.", type.toString()));
        }
    }

    private void logError(String field, String message){
        alerts.add(new ValidationAlert(
            ValidationAlert.Criticality.ERROR,
            message,
            ValidationType,
            field));
    }

    @Override
    public List<ValidationAlert> getAlerts() {
        return alerts;
    }
}
