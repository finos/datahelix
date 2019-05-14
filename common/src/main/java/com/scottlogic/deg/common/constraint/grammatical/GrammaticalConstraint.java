package com.scottlogic.deg.common.constraint.grammatical;

import com.scottlogic.deg.common.constraint.Constraint;

public interface GrammaticalConstraint extends Constraint {
    default Constraint negate()
    {
        return new NegatedGrammaticalConstraint(this);
    }
}
