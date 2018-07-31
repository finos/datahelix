package com.scottlogic.deg.generator.constraints;

import java.util.ArrayList;
import java.util.Collection;

public class OrConstraint implements IConstraint
{
    public final Collection<IConstraint> subConstraints;

    public OrConstraint(Collection<IConstraint> subConstraints)
    {
        this.subConstraints = subConstraints;
    }

    public OrConstraint(IConstraint constraintA, IConstraint constraintB) {
        subConstraints = new ArrayList<>();
        subConstraints.add(constraintA);
        subConstraints.add(constraintB);
    }
}
