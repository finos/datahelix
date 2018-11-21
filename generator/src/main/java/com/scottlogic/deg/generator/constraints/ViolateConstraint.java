package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

import java.util.Collection;

public class ViolateConstraint implements IConstraint {
    public final IConstraint violatedConstraint;

    public ViolateConstraint(IConstraint violatedConstraint) {
        this.violatedConstraint = violatedConstraint;
    }

    @Override
    public String toDotLabel() {
        throw new UnsupportedOperationException("VIOLATE constraints should be consumed during conversion to decision trees");
    }

    @Override
    public Collection<Field> getFields() {
        return violatedConstraint.getFields();
    }
}
