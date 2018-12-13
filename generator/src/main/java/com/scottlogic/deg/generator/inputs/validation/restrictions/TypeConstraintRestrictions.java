package com.scottlogic.deg.generator.inputs.validation.restrictions;

import com.scottlogic.deg.generator.constraints.atomic.IsOfTypeConstraint;
import com.scottlogic.deg.generator.inputs.validation.Criticality;
import com.scottlogic.deg.generator.inputs.validation.StandardValidationMessages;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.inputs.validation.ValidationType;
import com.scottlogic.deg.generator.inputs.validation.messages.TypeConstraintValidationMessages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class TypeConstraintRestrictions implements ConstraintValidation {

    public final ValidationType validationType = ValidationType.TYPE;
    private HashSet<IsOfTypeConstraint.Types> allowedTypes;
    private List<ValidationAlert> alerts;

    public TypeConstraintRestrictions(){
        this.allowedTypes = new HashSet<>(Arrays.asList(IsOfTypeConstraint.Types.values()));
        this.alerts = new ArrayList<>();

    }

    public void isOfType(String field, IsOfTypeConstraint.Types type) {

        if (this.allowedTypes.contains(type)) {
            this.allowedTypes = new HashSet<>(Arrays.asList(type));
        }
        else
        {
            logError(field, new TypeConstraintValidationMessages(allowedTypes.iterator().next(), type));
        }
    }

    private void logError(String field, StandardValidationMessages message){
        alerts.add(new ValidationAlert(
            Criticality.ERROR,
            message,
            validationType,
            field));
    }

    @Override
    public List<ValidationAlert> getAlerts() {
        return alerts;
    }
}
