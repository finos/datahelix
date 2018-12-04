package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

import java.util.Collection;

public class ViolateConstraint implements LogicalConstraint {
    public final LogicalConstraint violatedConstraint;

    public ViolateConstraint(LogicalConstraint violatedConstraint) {
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
