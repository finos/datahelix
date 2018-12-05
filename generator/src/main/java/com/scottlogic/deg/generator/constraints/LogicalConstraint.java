package com.scottlogic.deg.generator.constraints;

public interface LogicalConstraint extends Constraint {
    default Constraint negate()
    {
        return new NotConstraint(this);
    }
}
