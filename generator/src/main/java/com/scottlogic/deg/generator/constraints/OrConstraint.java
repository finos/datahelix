package com.scottlogic.deg.generator.constraints;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class OrConstraint implements IConstraint {
    public final Collection<IConstraint> subConstraints;

    public OrConstraint(Collection<IConstraint> subConstraints) {
        this.subConstraints = subConstraints;
    }

    public OrConstraint(IConstraint... subConstraints) {
        this(Arrays.asList(subConstraints));
    }

    @Override
    public String toDotLabel() {
        throw new UnsupportedOperationException("OR constraints should be consumed during conversion to decision trees");

//        return String.format("Or (%s)", subConstraints.stream()
//            .map(x -> x.toDotLabel()).collect(Collectors.joining(", ")));
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
