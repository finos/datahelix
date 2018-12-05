package com.scottlogic.deg.generator.constraints.gramatical;

import java.util.Objects;

public class GramaticalNotConstraint implements GramaticalConstraint {
    public final GramaticalConstraint negatedConstraint;

    protected GramaticalNotConstraint(GramaticalConstraint negatedConstraint) {
        this.negatedConstraint = negatedConstraint;
    }

    @Override
    public GramaticalConstraint negate() {
        return this.negatedConstraint;
    }

    private GramaticalConstraint getBaseConstraint(){
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
        GramaticalNotConstraint otherConstraint = (GramaticalNotConstraint) o;
        return Objects.equals(getBaseConstraint(), otherConstraint.getBaseConstraint());
    }

    @Override
    public int hashCode(){
        return Objects.hash("NOT", negatedConstraint.hashCode());
    }
}
