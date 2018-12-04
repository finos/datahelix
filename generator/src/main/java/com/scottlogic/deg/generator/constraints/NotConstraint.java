package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

import java.util.Collection;
import java.util.Objects;

public class NotConstraint implements LogicalConstraint {
    public final LogicalConstraint negatedConstraint;

    protected NotConstraint(LogicalConstraint negatedConstraint) {
        this.negatedConstraint = negatedConstraint;
    }

    @Override
    public LogicalConstraint not() {
        return this.negatedConstraint;
    }

    private LogicalConstraint getBaseConstraint(){
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
        /*Use the encoded character code for the NOT (¬) symbol; leaving it un-encoded causes issues with visualisers*/
        return String.format("&#x00AC;(%s)", negatedConstraint.toDotLabel());
    }

    @Override
    public Collection<Field> getFields() {
        return negatedConstraint.getFields();
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
