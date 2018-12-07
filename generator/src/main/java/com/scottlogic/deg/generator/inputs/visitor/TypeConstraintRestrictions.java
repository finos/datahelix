package com.scottlogic.deg.generator.inputs.visitor;

import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

class TypeConstraintRestrictions {

    public final ValidationAlert.ValidationType ValidationType = ValidationAlert.ValidationType.TYPE;
    private HashSet<IsOfTypeConstraint.Types> allowedTypes;

    public TypeConstraintRestrictions(){
        this.allowedTypes = new HashSet<>(Arrays.asList(IsOfTypeConstraint.Types.values()));
    }

    public List<ValidationAlert> IsOfType(String field, IsOfTypeConstraint.Types type) {

        List<ValidationAlert> alerts = new ArrayList<>();

        if (this.allowedTypes.contains(type)) {
            this.allowedTypes = new HashSet<>(Arrays.asList(type));
        }
        else
        {
            alerts.add(new ValidationAlert(
                ValidationAlert.Criticality.ERROR,
                String.format("Type %s is not allowed for this field.", type.toString()),
                ValidationType,
                field ));
        }

        return alerts;
    }
}
