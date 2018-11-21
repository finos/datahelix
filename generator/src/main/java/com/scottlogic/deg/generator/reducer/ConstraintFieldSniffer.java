package com.scottlogic.deg.generator.reducer;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;

import java.util.ArrayList;
import java.util.Collection;

public class ConstraintFieldSniffer {

    ConstraintAndFieldTuple generateTuple(IConstraint constraint) {
        final Field field = detectField(constraint);
        return new ConstraintAndFieldTuple(constraint, field);
    }

    public Field detectField(IConstraint constraint) {
        final Collection<Field> fields = constraint.getFields();
        if (fields.size() == 1) return new ArrayList<>(fields).get(0);
        throw new UnsupportedOperationException();
    }
}
