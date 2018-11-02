package com.scottlogic.deg.generator.constraints;

public class NotConstraint implements IConstraint {
    public final IConstraint negatedConstraint;

    public NotConstraint(IConstraint negatedConstraint) {
        this.negatedConstraint = negatedConstraint;
    }

    @Override
    public String toDotLabel() {
        return String.format("&#x00AC;(%s)", negatedConstraint.toDotLabel());
    }

    public String toString(){
        return String.format(
                "NOT(%s)",
                negatedConstraint);
    }
}
