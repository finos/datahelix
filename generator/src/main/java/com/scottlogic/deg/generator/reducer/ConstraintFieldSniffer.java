package com.scottlogic.deg.generator.reducer;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.IConstraint;

import java.util.Collection;

public class ConstraintFieldSniffer {

    ConstraintAndFieldTuple generateTuple(AtomicConstraint constraint) {
        return new ConstraintAndFieldTuple(constraint, constraint.getField());
    }

    public Field detectField(AtomicConstraint constraint) {
        return constraint.getField();
    }
}
