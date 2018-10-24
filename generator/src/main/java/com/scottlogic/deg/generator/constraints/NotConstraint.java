package com.scottlogic.deg.generator.constraints;

import java.util.Objects;

public class NotConstraint implements IConstraint {
    public final IConstraint negatedConstraint;

    public NotConstraint(IConstraint negatedConstraint) {
        this.negatedConstraint = negatedConstraint;
    }

    private IConstraint getBaseConstraint(){
        if (negatedConstraint instanceof NotConstraint){
            return ((NotConstraint) negatedConstraint).getBaseConstraint();
        }
        return negatedConstraint;
    }

    private int getNegationLevel(){
        if (negatedConstraint instanceof NotConstraint){
            return ((NotConstraint) negatedConstraint).getNegationLevel() + 1;
        }
        return 1;
    }

    @Override
    public String toDotLabel() {
        return String.format("¬(%s)", negatedConstraint.toDotLabel());
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
        NotConstraint otherConstraint = (NotConstraint) o;
        return Objects.equals(getBaseConstraint(), otherConstraint.getBaseConstraint())
            && Objects.equals(getNegationLevel() % 2, otherConstraint.getNegationLevel() % 2);
    }

    @Override
    public int hashCode(){
        return Objects.hash("NOT", negatedConstraint.hashCode());
    }
}
