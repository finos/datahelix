package com.scottlogic.deg.reducer;

import com.scottlogic.deg.constraint.IConstraint;
import com.scottlogic.deg.input.Field;
import com.scottlogic.deg.restriction.FieldSpec;
import com.scottlogic.deg.restriction.IRestrictionApplier;
import com.scottlogic.deg.restriction.RestrictionApplierFactory;
import com.scottlogic.deg.restriction.RowSpec;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ConstraintReducer {
    private final FieldRestrictionFactory fieldRestrictionFactory = new FieldRestrictionFactory();
    private final RestrictionApplierFactory restrictionApplierFactory = new RestrictionApplierFactory();

    public RowSpec getReducedConstraints(Iterable<IConstraint> constraints) {
        final Map<Field, List<IConstraint>> fieldConstraints = StreamSupport
                .stream(constraints.spliterator(), false)
                .collect(Collectors.groupingBy(IConstraint::getField));
        final List<FieldSpec> fieldSpecs = fieldConstraints.entrySet().stream()
                .map(x -> getReducedConstraints(x.getKey(), x.getValue()))
                .collect(Collectors.toList());
        return new RowSpec(fieldSpecs);
    }

    private FieldSpec getReducedConstraints(Field field, Iterable<IConstraint> constraints) {
        FieldSpec fieldRestriction = null;
        IRestrictionApplier restrictionApplier = null;
        for (IConstraint constraint : constraints) {
            if (fieldRestriction == null) {
                fieldRestriction = fieldRestrictionFactory.getForConstraint(field, constraint);
                restrictionApplier = restrictionApplierFactory.getRestrictionApplier(fieldRestriction);
            }
            restrictionApplier.apply(fieldRestriction, constraint);
        }
        if (fieldRestriction == null) {
            throw new IllegalStateException();
        }
        return fieldRestriction;
    }

}

