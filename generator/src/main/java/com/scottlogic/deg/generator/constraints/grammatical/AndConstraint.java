package com.scottlogic.deg.generator.constraints.grammatical;

import com.scottlogic.deg.generator.constraints.Constraint;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class AndConstraint implements GrammaticalConstraint
{
    public final Collection<Constraint> subConstraints;

    public AndConstraint(Collection<Constraint> subConstraints) {
        this.subConstraints = subConstraints;
    }

    public AndConstraint(Constraint... subConstraints) {
        this(Arrays.asList(subConstraints));
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AndConstraint otherConstraint = (AndConstraint) o;
        return Objects.equals(subConstraints, otherConstraint.subConstraints);
    }

    @Override
    public int hashCode(){
        return Objects.hash("AND", subConstraints);
    }
}
