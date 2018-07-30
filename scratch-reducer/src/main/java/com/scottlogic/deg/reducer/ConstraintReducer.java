package com.scottlogic.deg.reducer;

import com.scottlogic.deg.constraint.IConstraint;
import com.scottlogic.deg.input.Field;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ConstraintReducer {
    private final FieldRestrictionFactory fieldRestrictionFactory = new FieldRestrictionFactory();

    public List<IFieldRestriction> getReducedConstraints(Iterable<IConstraint> constraints) {
        final Map<Field, List<IConstraint>> fieldConstraints = StreamSupport
                .stream(constraints.spliterator(), false)
                .collect(Collectors.groupingBy(IConstraint::getField));
        return fieldConstraints.entrySet().stream()
                .map(x -> getReducedConstraints(x.getKey(), x.getValue()))
                .collect(Collectors.toList());
    }

    public IFieldRestriction getReducedConstraints(Field field, Iterable<IConstraint> constraints) {
        IFieldRestriction fieldRestriction = null;
        for (IConstraint constraint : constraints) {
            if (fieldRestriction == null) {
                fieldRestriction = fieldRestrictionFactory.getForConstraint(field, constraint);
            }
        }
        return fieldRestriction;
    }

}

