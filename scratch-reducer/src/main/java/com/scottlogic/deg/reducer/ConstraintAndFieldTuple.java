package com.scottlogic.deg.reducer;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;

public class ConstraintAndFieldTuple {
    private final Field field;
    private final IConstraint constraint;

    public ConstraintAndFieldTuple(IConstraint constraint, Field field) {
        this.constraint = constraint;
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    public IConstraint getConstraint() {
        return constraint;
    }
}
