package com.scottlogic.deg.generator.constraints.gramatical;

import com.scottlogic.deg.generator.constraints.Constraint;

public interface LogicalConstraint extends Constraint {
    default Constraint negate()
    {
        return new NotConstraint(this);
    }
}
