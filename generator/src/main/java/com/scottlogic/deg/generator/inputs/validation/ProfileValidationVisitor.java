package com.scottlogic.deg.generator.inputs.validation;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.inputs.validation.restrictions.*;
import com.scottlogic.deg.generator.restrictions.Nullness;

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

        state.typeConstraintValidationMergeOperation.isOfType(constraint.field.name, constraint.requiredType);
    }

    @Override
    public void visit(IsAfterConstantDateTimeConstraint constraint) {
        ConstraintRestrictions state = getFieldState(constraint.field.name);

        state.typeConstraintValidationMergeOperation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.TEMPORAL);
        state.temporalConstraintRestrictions.isAfter(constraint.field.name, constraint.referenceValue, false);

    }

    @Override
    public void visit(IsBeforeConstantDateTimeConstraint constraint) {
        ConstraintRestrictions state = getFieldState(constraint.field.name);

        state.typeConstraintValidationMergeOperation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.TEMPORAL);
        state.temporalConstraintRestrictions.isBefore(constraint.field.name, constraint.referenceValue, false);

    }

    @Override
    public void visit(IsAfterOrEqualToConstantDateTimeConstraint constraint) {
        ConstraintRestrictions state = getFieldState(constraint.field.name);

        state.typeConstraintValidationMergeOperation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.TEMPORAL);
        state.temporalConstraintRestrictions.isAfter(constraint.field.name, constraint.referenceValue, true);
    }

    @Override
    public void visit(IsBeforeOrEqualToConstantDateTimeConstraint constraint) {

        ConstraintRestrictions state = getFieldState(constraint.field.name);

        state.typeConstraintValidationMergeOperation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.TEMPORAL);
        state.temporalConstraintRestrictions.isBefore(constraint.field.name, constraint.referenceValue, true);
    }

    @Override
    public void visit(IsInSetConstraint constraint) {

        ConstraintRestrictions state = getFieldState(constraint.field.name);
        state.nullConstraintValidationMergeOperation.setNullness(constraint.field.name, Nullness.MUST_NOT_BE_NULL);
        state.setConstraintValidationMergeOperation.isInSet(constraint.field.name, constraint.legalValues);


    }

    @Override
    public void visit(IsStringShorterThanConstraint constraint) {
        ConstraintRestrictions state = getFieldState(constraint.field.name);

        state.typeConstraintValidationMergeOperation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.STRING);
        state.stringConstraintValidationMergeOperation.isShorterThan(constraint.field.name, constraint.referenceValue);

    }

    @Override
    public void visit(IsStringLongerThanConstraint constraint) {
        ConstraintRestrictions state = getFieldState(constraint.field.name);

       state.typeConstraintValidationMergeOperation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.STRING);
       state.stringConstraintValidationMergeOperation.isLongerThan(constraint.field.name, constraint.referenceValue);


    }

    @Override
    public void visit(IsNullConstraint constraint) {
        ConstraintRestrictions state = getFieldState(constraint.field.name);

        state.nullConstraintValidationMergeOperation.setNullness(constraint.field.name, Nullness.MUST_BE_NULL);
    }

    @Override
    public void visit(NotConstraint constraint) {
        AtomicConstraint negatedConstraint = constraint.negatedConstraint;

        ConstraintRestrictions state = getFieldState(negatedConstraint.getField().name);

        if(negatedConstraint instanceof IsNullConstraint){
            state.nullConstraintValidationMergeOperation.setNullness(negatedConstraint.getField().name, Nullness.MUST_NOT_BE_NULL);
        } else if(negatedConstraint instanceof IsInSetConstraint){
            IsInSetConstraint negatedSetConstraint = (IsInSetConstraint) negatedConstraint;
            state.setConstraintValidationMergeOperation.mustNotBeInSet(negatedSetConstraint.getField().name,negatedSetConstraint.legalValues);
        }
    }

    @Override
    public void visit(IsGranularToConstraint constraint) {

        ConstraintRestrictions state = getFieldState(constraint.getField().name);
        state.typeConstraintValidationMergeOperation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.NUMERIC);
        state.granularityConstraintValidationMergeOperation.granularTo(constraint.field.name, constraint.granularity.getNumericGranularity());

    }

    @Override
    public void visit(IsLessThanConstantConstraint constraint) {
        ConstraintRestrictions state = getFieldState(constraint.getField().name);

        state.typeConstraintValidationMergeOperation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.NUMERIC);
        state.numericConstraintRestriction.IsLessThan(constraint.field.name, constraint.referenceValue, false);
    }

    @Override
    public void visit(IsGreaterThanConstantConstraint constraint) {
        ConstraintRestrictions state = getFieldState(constraint.getField().name);

        state.typeConstraintValidationMergeOperation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.NUMERIC);
        state.numericConstraintRestriction.IsGreaterThan(constraint.field.name, constraint.referenceValue, false);

    }

    @Override
    public void visit(IsLessThanOrEqualToConstantConstraint constraint) {
        ConstraintRestrictions state = getFieldState(constraint.getField().name);

        state.typeConstraintValidationMergeOperation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.NUMERIC);
        state.numericConstraintRestriction.IsLessThan(constraint.field.name, constraint.referenceValue, true);
    }

    @Override
    public void visit(IsGreaterThanOrEqualToConstantConstraint constraint) {
        ConstraintRestrictions state = getFieldState(constraint.getField().name);

        state.typeConstraintValidationMergeOperation.isOfType(constraint.field.name, IsOfTypeConstraint.Types.NUMERIC);
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


    private ConstraintRestrictions getFieldState(String fieldName) {
        if (allFieldsState.containsKey(fieldName)) {
            return allFieldsState.get(fieldName);
        } else {
            ConstraintRestrictions noRestrictions = new ConstraintRestrictions(
                new TypeConstraintValidationMergeOperation(),
                new TemporalConstraintRestrictions(),
                new SetConstraintValidationMergeOperation(),
                new StringConstraintValidationMergeOperation(),
                new NullConstraintValidationMergeOperation(),
                new GranularityConstraintValidationMergeOperation(),
                new NumericConstraintValidationMergeOperation());

            allFieldsState.put(fieldName, noRestrictions);
            return noRestrictions;
        }
    }


}
