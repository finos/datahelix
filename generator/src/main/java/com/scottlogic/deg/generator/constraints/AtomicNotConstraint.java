package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

import java.util.Objects;

public class AtomicNotConstraint implements AtomicConstraint {
    public final AtomicConstraint negatedConstraint;

    protected AtomicNotConstraint(AtomicConstraint negatedConstraint) {
        this.negatedConstraint = negatedConstraint;
    }

    @Override
    public AtomicConstraint negate() {
        return this.negatedConstraint;
    }

    private AtomicConstraint getBaseConstraint(){
        return negatedConstraint;
    }

    @Override
    public String toDotLabel() {
        /*Use the encoded character code for the NOT (¬) symbol; leaving it un-encoded causes issues with visualisers*/
        return String.format("&#x00AC;(%s)", negatedConstraint.toDotLabel());
    }

    @Override
    public Field getField() {
        return negatedConstraint.getField();
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
        AtomicNotConstraint otherConstraint = (AtomicNotConstraint) o;
        return Objects.equals(getBaseConstraint(), otherConstraint.getBaseConstraint());
    }

    @Override
    public int hashCode(){
        return Objects.hash("NOT", negatedConstraint.hashCode());
    }
}
