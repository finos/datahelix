package com.scottlogic.deg.generator.reducer;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.AtomicConstraint;

class ConstraintAndFieldTuple {
    private final Field field;
    private final AtomicConstraint constraint;

    ConstraintAndFieldTuple(AtomicConstraint constraint, Field field) {
        this.constraint = constraint;
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    public AtomicConstraint getConstraint() {
        return constraint;
    }
}
