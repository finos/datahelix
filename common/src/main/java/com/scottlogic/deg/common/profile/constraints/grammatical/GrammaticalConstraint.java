package com.scottlogic.deg.common.profile.constraints.grammatical;

import com.scottlogic.deg.common.profile.constraints.Constraint;

public interface GrammaticalConstraint extends Constraint {
    default Constraint negate()
    {
        return new NegatedGrammaticalConstraint(this);
    }
}
