package com.scottlogic.deg.generator.inputs.validation;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.inputs.validation.validators.*;
import com.scottlogic.deg.generator.restrictions.Nullness;

import java.util.*;
import java.util.stream.Collectors;

public class ProfileValidationVisitor implements ProfileVisitor {

    private Map<String, ConstraintValidator> allFieldsState;

    public ProfileValidationVisitor() {
        allFieldsState = new HashMap<>();
    }

    @Override
    public void visit(IsOfTypeConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.field.name);

        state.typeConstraintValidation.isOfType(constraint.field.name, constraint.requiredType);
    }

    @Override
    public void visit(IsAfterConstantDateTimeConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.field.name);

        state.typeConstraintValidation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.TEMPORAL);
        state.temporalConstraintValidation.isAfter(constraint.field.name, constraint.referenceValue, false);

    }

    @Override
    public void visit(IsBeforeConstantDateTimeConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.field.name);

        state.typeConstraintValidation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.TEMPORAL);
        state.temporalConstraintValidation.isBefore(constraint.field.name, constraint.referenceValue, false);

    }

    @Override
    public void visit(IsAfterOrEqualToConstantDateTimeConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.field.name);

        state.typeConstraintValidation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.TEMPORAL);
        state.temporalConstraintValidation.isAfter(constraint.field.name, constraint.referenceValue, true);
    }

    @Override
    public void visit(IsBeforeOrEqualToConstantDateTimeConstraint constraint) {

        ConstraintValidator state = getFieldState(constraint.field.name);

        state.typeConstraintValidation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.TEMPORAL);
        state.temporalConstraintValidation.isBefore(constraint.field.name, constraint.referenceValue, true);
    }

    @Override
    public void visit(IsInSetConstraint constraint) {

        ConstraintValidator state = getFieldState(constraint.field.name);
        state.setConstraintValidation.isInSet(constraint.field.name, constraint.legalValues);


    }

    @Override
    public void visit(IsStringShorterThanConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.field.name);

        state.typeConstraintValidation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.STRING);
        state.stringConstraintValidation.isShorterThan(constraint.field.name, constraint.referenceValue);

    }

    @Override
    public void visit(IsStringLongerThanConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.field.name);

       state.typeConstraintValidation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.STRING);
       state.stringConstraintValidation.isLongerThan(constraint.field.name, constraint.referenceValue);


    }

    @Override
    public void visit(IsNullConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.field.name);

        state.nullConstraintValidation.setNullness(constraint.field.name, Nullness.MUST_BE_NULL);
    }

    @Override
    public void visit(NotConstraint constraint) {
        AtomicConstraint negatedConstraint = constraint.negatedConstraint;

        ConstraintValidator state = getFieldState(negatedConstraint.getField().name);

        if(negatedConstraint instanceof IsNullConstraint){
            state.nullConstraintValidation.setNullness(negatedConstraint.getField().name, Nullness.MUST_NOT_BE_NULL);
        } else if(negatedConstraint instanceof IsInSetConstraint){
            IsInSetConstraint negatedSetConstraint = (IsInSetConstraint) negatedConstraint;
            state.setConstraintValidation.mustNotBeInSet(negatedSetConstraint.getField().name, negatedSetConstraint.legalValues);
        }
    }

    @Override
    public void visit(IsGranularToConstraint constraint) {

        ConstraintValidator state = getFieldState(constraint.getField().name);
        state.typeConstraintValidation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.NUMERIC);
        state.granularityConstraintValidation.granularTo(constraint.field.name, constraint.granularity.getNumericGranularity());

    }

    @Override
    public void visit(IsLessThanConstantConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.getField().name);

        state.typeConstraintValidation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.NUMERIC);
        state.numericConstraintRestriction.IsLessThan(constraint.field.name, constraint.referenceValue, false);
    }

    @Override
    public void visit(IsGreaterThanConstantConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.getField().name);

        state.typeConstraintValidation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.NUMERIC);
        state.numericConstraintRestriction.IsGreaterThan(constraint.field.name, constraint.referenceValue, false);

    }

    @Override
    public void visit(IsLessThanOrEqualToConstantConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.getField().name);

        state.typeConstraintValidation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.NUMERIC);
        state.numericConstraintRestriction.IsLessThan(constraint.field.name, constraint.referenceValue, true);
    }

    @Override
    public void visit(IsGreaterThanOrEqualToConstantConstraint constraint) {
        ConstraintValidator state = getFieldState(constraint.getField().name);

        state.typeConstraintValidation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.NUMERIC);
        state.numericConstraintRestriction.IsGreaterThan(constraint.field.name, constraint.referenceValue, true);
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


    private ConstraintValidator getFieldState(String fieldName) {
        if (allFieldsState.containsKey(fieldName)) {
            return allFieldsState.get(fieldName);
        } else {
            ConstraintValidator noRestrictions = new ConstraintValidator(
                new TypeConstraintValidator(),
                new TemporalConstraintValidator(),
                new SetConstraintValidator(),
                new StringConstraintValidator(),
                new NullConstraintValidator(),
                new GranularityConstraintValidator(),
                new NumericConstraintValidator());

            allFieldsState.put(fieldName, noRestrictions);
            return noRestrictions;
        }
    }


}
