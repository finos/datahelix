package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.IConstraint;

import java.util.Collection;

public class Rule
{
    public final String description;
    public final Collection<IConstraint> constraints;

    public Rule(String description, Collection<IConstraint> constraints)
    {
        this.description = description;
        this.constraints = constraints;
    }
}
