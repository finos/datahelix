package com.scottlogic.deg.generator.constraints.gramatical;

import com.scottlogic.deg.generator.constraints.Constraint;

public interface GramaticalConstraint extends Constraint {
    default Constraint negate()
    {
        return new NotConstraint(this);
    }
}
