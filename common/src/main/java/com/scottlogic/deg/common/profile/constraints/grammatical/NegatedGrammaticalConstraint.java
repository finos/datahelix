package com.scottlogic.deg.common.profile.constraints.grammatical;

import com.scottlogic.deg.common.profile.RuleInformation;

import java.util.Objects;
import java.util.Set;

public class NegatedGrammaticalConstraint implements GrammaticalConstraint {
    private final GrammaticalConstraint negatedConstraint;

    NegatedGrammaticalConstraint(GrammaticalConstraint negatedConstraint) {
        if (negatedConstraint instanceof NegatedGrammaticalConstraint)
            throw new IllegalArgumentException("nested NegatedGrammatical constraint not allowed");
        this.negatedConstraint = negatedConstraint;
    }

    @Override
    public GrammaticalConstraint negate() {
        return this.negatedConstraint;
    }

    private GrammaticalConstraint getBaseConstraint(){
        return negatedConstraint;
    }

    public String toString(){
        return String.format(
                "NOT(%s)",
                negatedConstraint);
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NegatedGrammaticalConstraint otherConstraint = (NegatedGrammaticalConstraint) o;
        return Objects.equals(getBaseConstraint(), otherConstraint.getBaseConstraint());
    }

    @Override
    public int hashCode(){
        return Objects.hash("NOT", negatedConstraint.hashCode());
    }

    public GrammaticalConstraint getNegatedConstraint() {
        return negatedConstraint;
    }
}
