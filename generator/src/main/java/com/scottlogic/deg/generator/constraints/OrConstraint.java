package com.scottlogic.deg.generator.constraints;

import java.util.Arrays;
import java.util.Collection;

public class OrConstraint implements IConstraint
{
    public final Collection<IConstraint> subConstraints;

    public OrConstraint(Collection<IConstraint> subConstraints) {
        this.subConstraints = subConstraints;
    }

    public OrConstraint(IConstraint... subConstraints) {
        this(Arrays.asList(subConstraints));
    }
}
