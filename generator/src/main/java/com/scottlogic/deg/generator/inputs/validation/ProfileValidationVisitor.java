package com.scottlogic.deg.generator.inputs.validation;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.atomic.*;

import java.util.*;
import java.util.stream.Collectors;

public class ProfileValidationVisitor implements ProfileVisitor {

    private Map<String, ConstraintRestrictions> allFieldsState;

    public ProfileValidationVisitor() {
        allFieldsState = new HashMap<>();
    }

    @Override
    public void visit(IsOfTypeConstraint constraint) {
        ConstraintRestrictions state = getFieldState(constraint.field.name);

        state.typeConstraintRestrictions.IsOfType(constraint.field.name, constraint.requiredType);
    }

    @Override
    public void visit(IsAfterConstantDateTimeConstraint constraint) {
        ConstraintRestrictions state = getFieldState(constraint.field.name);

        state.typeConstraintRestrictions.IsOfType(constraint.field.name, IsOfTypeConstraint.Types.TEMPORAL);
        state.temporalConstraintRestrictions.IsAfter(constraint.field.name, constraint.referenceValue);

    }

    @Override
    public void visit(IsBeforeConstantDateTimeConstraint constraint) {
        ConstraintRestrictions state = getFieldState(constraint.field.name);

        state.typeConstraintRestrictions.IsOfType(constraint.field.name, IsOfTypeConstraint.Types.TEMPORAL);
        state.temporalConstraintRestrictions.IsBefore(constraint.field.name, constraint.referenceValue);

    }

    @Override
    public void visit(IsInSetConstraint constraint) {

        ConstraintRestrictions state = getFieldState(constraint.field.name);
        state.setConstraintRestrictions.IsInSet(constraint.field.name, constraint.legalValues);


    }

    @Override
    public void visit(IsStringShorterThanConstraint constraint) {
        ConstraintRestrictions state = getFieldState(constraint.field.name);

        state.typeConstraintRestrictions.IsOfType(constraint.field.name, IsOfTypeConstraint.Types.STRING);
        state.stringConstraintRestrictions.IsShorterThan(constraint.field.name, constraint.referenceValue);

    }

    @Override
    public void visit(IsStringLongerThanConstraint constraint) {
        ConstraintRestrictions state = getFieldState(constraint.field.name);

       state.typeConstraintRestrictions.IsOfType(constraint.field.name, IsOfTypeConstraint.Types.STRING);
       state.stringConstraintRestrictions.IsLongerThan(constraint.field.name, constraint.referenceValue);


    }

    @Override
    public void visit(ProfileFields fields) {

    }

    @Override
    public void visit(Rule rule) {

    }

    @Override
    public void visit(Constraint constraint) {

    }

    public void outputValidationResults() {
        final List<ValidationAlert> alerts = new ArrayList<>();
        allFieldsState.values().stream().map(state-> alerts.addAll(state.getValidationAlerts())).collect(Collectors.toList());

        if (alerts.size() > 0) {
            boolean hasErrors = false;
            for (ValidationAlert alert : alerts) {

                if (alert.getCriticality().equals(ValidationAlert.Criticality.ERROR)) {
                    hasErrors = true;
                }

                System.out.println(alert.toString());
            }

            if (hasErrors) {
                System.out.println("Encountered unrecoverable profile validation errors.");
                System.exit(1);
            }
        }
    }


    private ConstraintRestrictions getFieldState(String fieldName) {
        if (allFieldsState.containsKey(fieldName)) {
            return allFieldsState.get(fieldName);
        } else {
            ConstraintRestrictions noRestrictions = new ConstraintRestrictions(
                new TypeConstraintRestrictions(),
                new TemporalConstraintRestrictions(),
                new SetConstraintRestrictions(),
                new StringConstraintRestrictions());

            allFieldsState.put(fieldName, noRestrictions);
            return noRestrictions;
        }
    }


}
