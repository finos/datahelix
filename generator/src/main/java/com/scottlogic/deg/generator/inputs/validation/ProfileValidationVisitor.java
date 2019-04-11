package com.scottlogic.deg.generator.inputs.validation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.inputs.validation.validators.*;
import com.scottlogic.deg.generator.restrictions.Nullness;

import java.util.*;
import java.util.stream.Collectors;

public class ProfileValidationVisitor implements ProfileVisitor {

    private Map<Field, ConstraintValidator> allFieldsState;

    public ProfileValidationVisitor() {
        allFieldsState = new HashMap<>();
    }

    @Override
    public void visit(IsOfTypeConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.getField());

        state.typeConstraintValidator.isOfType(constraint.getField(), constraint.requiredType);
    }

    @Override
    public void visit(IsAfterConstantDateTimeConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.getField());

        state.typeConstraintValidator.isOfType(constraint.getField(), IsOfTypeConstraint.Types.DATETIME);
        state.dateTimeConstraintValidator.isAfter(constraint.getField(), constraint.referenceValue, false);
    }

    @Override
    public void visit(IsBeforeConstantDateTimeConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.getField());

        state.typeConstraintValidator.isOfType(constraint.getField(), IsOfTypeConstraint.Types.DATETIME);
        state.dateTimeConstraintValidator.isBefore(constraint.getField(), constraint.referenceValue, false);
    }

    @Override
    public void visit(IsAfterOrEqualToConstantDateTimeConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.getField());

        state.typeConstraintValidator.isOfType(constraint.getField(), IsOfTypeConstraint.Types.DATETIME);
        state.dateTimeConstraintValidator.isAfter(constraint.getField(), constraint.referenceValue, true);
    }

    @Override
    public void visit(IsBeforeOrEqualToConstantDateTimeConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.getField());

        state.typeConstraintValidator.isOfType(constraint.getField(), IsOfTypeConstraint.Types.DATETIME);
        state.dateTimeConstraintValidator.isBefore(constraint.getField(), constraint.referenceValue, true);
    }

    @Override
    public void visit(IsInSetConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.getField());

        state.setConstraintValidator.isInSet(constraint.getField(), constraint.legalValues);
    }

    @Override
    public void visit(IsStringShorterThanConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.getField());

        state.typeConstraintValidator.isOfType(constraint.getField(), IsOfTypeConstraint.Types.STRING);
        state.stringConstraintValidator.isShorterThan(constraint.getField(), constraint.referenceValue);
    }

    @Override
    public void visit(IsStringLongerThanConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.getField());

        state.typeConstraintValidator.isOfType(constraint.getField(), IsOfTypeConstraint.Types.STRING);
        state.stringConstraintValidator.isLongerThan(constraint.getField(), constraint.referenceValue);
    }

    @Override
    public void visit(IsNullConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.getField());

        state.nullConstraintValidator.setNullness(constraint.getField(), Nullness.MUST_BE_NULL);
    }

    @Override
    public void visit(NotConstraint constraint) {
        AtomicConstraint negatedConstraint = constraint.negatedConstraint;

        ConstraintValidator state = getFieldState(negatedConstraint.getField());

        if(negatedConstraint instanceof IsNullConstraint){
            state.nullConstraintValidator.setNullness(negatedConstraint.getField(), Nullness.MUST_NOT_BE_NULL);
        } else if(negatedConstraint instanceof IsInSetConstraint){
            IsInSetConstraint negatedSetConstraint = (IsInSetConstraint) negatedConstraint;
            state.setConstraintValidator.mustNotBeInSet(negatedSetConstraint.getField(), negatedSetConstraint.legalValues);
        }
    }

    @Override
    public void visit(IsGranularToConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.getField());

        state.typeConstraintValidator.isOfType(constraint.getField(), IsOfTypeConstraint.Types.NUMERIC);
    }

    @Override
    public void visit(IsLessThanConstantConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.getField());

        state.typeConstraintValidator.isOfType(constraint.getField(), IsOfTypeConstraint.Types.NUMERIC);
        state.numericConstraintValidator.IsLessThan(constraint.getField(), constraint.referenceValue, false);
    }

    @Override
    public void visit(IsGreaterThanConstantConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.getField());

        state.typeConstraintValidator.isOfType(constraint.getField(), IsOfTypeConstraint.Types.NUMERIC);
        state.numericConstraintValidator.IsGreaterThan(constraint.getField(), constraint.referenceValue, false);
    }

    @Override
    public void visit(IsLessThanOrEqualToConstantConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.getField());

        state.typeConstraintValidator.isOfType(constraint.getField(), IsOfTypeConstraint.Types.NUMERIC);
        state.numericConstraintValidator.IsLessThan(constraint.getField(), constraint.referenceValue, true);
    }

    @Override
    public void visit(IsGreaterThanOrEqualToConstantConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.getField());

        state.typeConstraintValidator.isOfType(constraint.getField(), IsOfTypeConstraint.Types.NUMERIC);
        state.numericConstraintValidator.IsGreaterThan(constraint.getField(), constraint.referenceValue, true);
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

    public List<ValidationAlert> getAlerts() {
        final List<ValidationAlert> alerts = new ArrayList<>();
        allFieldsState.values().stream().map(state-> alerts.addAll(state.getValidationAlerts())).collect(Collectors.toList());

        return alerts;
    }


    private ConstraintValidator getFieldState(Field field) {
        if (allFieldsState.containsKey(field)) {
            return allFieldsState.get(field);
        } else {
            ConstraintValidator noRestrictions = new ConstraintValidator(
                new TypeConstraintValidator(),
                new DateTimeConstraintValidator(),
                new SetConstraintValidator(),
                new StringConstraintValidator(),
                new NullConstraintValidator(),
                new NumericConstraintValidator());

            allFieldsState.put(field, noRestrictions);
            return noRestrictions;
        }
    }


}
