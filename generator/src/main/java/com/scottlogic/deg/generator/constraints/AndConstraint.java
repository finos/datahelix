package com.scottlogic.deg.generator.constraints;

import java.util.Collection;

public class AndConstraint implements IConstraint
{
    public final Collection<IConstraint> subConstraints;

    public AndConstraint(Collection<IConstraint> subConstraints)
    {
        this.subConstraints = subConstraints;
    }
}
