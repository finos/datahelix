package com.scottlogic.deg.generator.reducer;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;

import java.util.Collection;

public class ConstraintFieldSniffer {

    ConstraintAndFieldTuple generateTuple(IConstraint constraint) {
        final Field field = detectField(constraint);
        return new ConstraintAndFieldTuple(constraint, field);
    }

    public Field detectField(IConstraint constraint) {
        final Collection<Field> fields = constraint.getFields();
        if (fields.size() == 1) fields.iterator().next();
        throw new UnsupportedOperationException();
    }
}
