package com.scottlogic.deg.generator.constraints;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class OrConstraint implements LogicalConstraint {
    public final Collection<LogicalConstraint> subConstraints;

    public OrConstraint(Collection<LogicalConstraint> subConstraints) {
        this.subConstraints = subConstraints;
    }

    public OrConstraint(LogicalConstraint... subConstraints) {
        this(Arrays.asList(subConstraints));
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrConstraint otherConstraint = (OrConstraint) o;
        return Objects.equals(subConstraints, otherConstraint.subConstraints);
    }

    @Override
    public int hashCode(){
        return Objects.hash("OR", subConstraints);
    }
}
