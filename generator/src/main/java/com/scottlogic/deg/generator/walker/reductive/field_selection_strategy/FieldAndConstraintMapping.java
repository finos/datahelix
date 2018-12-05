package com.scottlogic.deg.generator.walker.reductive.field_selection_strategy;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.*;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class FieldAndConstraintMapping {
    private final Field field;
    private final Collection<IConstraint> constraints;
    private final int priority;

    public Field getField() {
        return field;
    }

    public Collection<IConstraint> getConstraints() {
        return constraints;
    }

    int getPriority() {
        return priority;
    }

    FieldAndConstraintMapping(Field field, Collection<IConstraint> constraints) {
        this.field = field;
        this.constraints = constraints;
        this.priority = getConstraintPriority(constraints);
    }

    private static int getConstraintPriority(Collection<IConstraint> constraints) {
        Set<Object> allLegalValuesForField = constraints
            .stream()
            .filter(c -> (c instanceof IsInSetConstraint))
            .map(c -> (IsInSetConstraint)c)
            .flatMap(setConstraint -> setConstraint.legalValues.stream())
            .collect(Collectors.toSet());

        int setConstraintPriority = allLegalValuesForField.isEmpty()
            ? 0
            : 20000 / allLegalValuesForField.size();

        return constraints
            .stream()
            .filter(c -> !(c instanceof IsInSetConstraint))
            .reduce(setConstraintPriority, (prev, c) -> prev + getConstraintPriority(c), (a, b) -> a + b);
    }

    private static int getConstraintPriority(IConstraint constraint) {
        if (constraint instanceof IsEqualToConstantConstraint
            || constraint instanceof IsNullConstraint) {
            return 20000;
        }

        if (constraint instanceof MatchesRegexConstraint
        || constraint instanceof IsGreaterThanConstantConstraint
        || constraint instanceof IsLessThanConstantConstraint
        || constraint instanceof IsStringLongerThanConstraint
        || constraint instanceof IsStringShorterThanConstraint
        || constraint instanceof IsAfterConstantDateTimeConstraint
        || constraint instanceof IsBeforeConstantDateTimeConstraint
        || constraint instanceof IsAfterOrEqualToConstantDateTimeConstraint
        || constraint instanceof IsBeforeOrEqualToConstantDateTimeConstraint) {
            return 500;
        }

        if (constraint instanceof IsOfTypeConstraint){
            return 250;
        }

        return 0;
    }

    @Override
    public String toString() {
        return String.format("%s: %d constraint/s (p:%d)", this.field, this.constraints.size(), this.priority);
    }
}
