package com.scottlogic.deg.generator.constraints;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class AndConstraint implements IConstraint
{
    public final Collection<IConstraint> subConstraints;

    public AndConstraint(Collection<IConstraint> subConstraints) {
        this.subConstraints = subConstraints;
    }

    public AndConstraint(IConstraint... subConstraints) {
        this(Arrays.asList(subConstraints));
    }

    @Override
    public String toDotLabel(){
        return String.format("And (%s)", subConstraints.stream()
            .map(x -> x.toDotLabel()).collect(Collectors.joining(", ")));
    }
}
