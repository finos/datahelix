package com.scottlogic.deg.generator.constraints.grammatical;

import com.scottlogic.deg.generator.constraints.Constraint;

public interface GrammaticalConstraint extends Constraint {
    default Constraint negate()
    {
        return new NegatedGrammaticalConstraint(this);
    }
}
