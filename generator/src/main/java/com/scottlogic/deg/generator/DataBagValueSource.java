package com.scottlogic.deg.generator;

import com.scottlogic.deg.common.constraint.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;

import java.util.Collections;
import java.util.Set;

public class DataBagValueSource {
    public static final DataBagValueSource Empty = new DataBagValueSource();

    private final Set<AtomicConstraint> constraints;
    private final Set<AtomicConstraint> violatedConstraints;

    private DataBagValueSource() {
        constraints = Collections.emptySet();
        violatedConstraints = Collections.emptySet();
    }

    public DataBagValueSource(FieldSpecSource fieldSpecSource) {
        this.constraints = fieldSpecSource != null ? fieldSpecSource.getConstraints() : null;
        this.violatedConstraints = fieldSpecSource != null ? fieldSpecSource.getViolatedConstraints() : null;
    }

    public Set<AtomicConstraint> getConstraints() {
        return constraints;
    }

    public Set<AtomicConstraint> getViolatedConstraints() {
        return violatedConstraints;
    }
}
