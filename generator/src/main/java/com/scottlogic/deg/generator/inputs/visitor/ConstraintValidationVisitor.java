package com.scottlogic.deg.generator.inputs.visitor;

import com.scottlogic.deg.generator.constraints.IsAfterConstantDateTimeConstraint;
import com.scottlogic.deg.generator.constraints.IsBeforeConstantDateTimeConstraint;
import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstraintValidationVisitor implements IConstraintValidatorVisitor {

    private Map<String, ConstraintRestrictions> allFieldsState;
    public ConstraintValidationVisitor() {
        allFieldsState = new HashMap<>();
    }

    @Override
    public List<ValidationAlert> visit(IsOfTypeConstraint constraint) {
        ConstraintRestrictions state = getFieldState(constraint.field.name);
        List<ValidationAlert> alerts = new ArrayList<>();

        alerts.addAll(state.typeConstraintRestrictions.IsOfType(constraint.field.name, constraint.requiredType));

        return alerts;
    }

    @Override
    public List<ValidationAlert> visit(IsAfterConstantDateTimeConstraint constraint) {
        ConstraintRestrictions state = getFieldState(constraint.field.name);
        List<ValidationAlert> alerts = new ArrayList<>();

        alerts.addAll(state.typeConstraintRestrictions.IsOfType(constraint.field.name, IsOfTypeConstraint.Types.TEMPORAL));
        alerts.addAll(state.temporalConstraintRestrictions.IsAfter(constraint.field.name, constraint.referenceValue));


        return alerts;
    }

    @Override
    public List<ValidationAlert> visit(IsBeforeConstantDateTimeConstraint constraint) {
        ConstraintRestrictions state = getFieldState(constraint.field.name);
        List<ValidationAlert> alerts = new ArrayList<>();

        alerts.addAll(state.typeConstraintRestrictions.IsOfType(constraint.field.name, IsOfTypeConstraint.Types.TEMPORAL));
        alerts.addAll(state.temporalConstraintRestrictions.IsBefore(constraint.field.name, constraint.referenceValue));

        return alerts;
    }

    private ConstraintRestrictions getFieldState(String fieldName) {
        if (allFieldsState.containsKey(fieldName)) {
            return allFieldsState.get(fieldName);
        } else {
            ConstraintRestrictions noRestrictions = new ConstraintRestrictions(
                new TypeConstraintRestrictions(),
                new TemporalConstraintRestrictions());

            allFieldsState.put(fieldName, noRestrictions);
            return noRestrictions;
        }
    }
}




