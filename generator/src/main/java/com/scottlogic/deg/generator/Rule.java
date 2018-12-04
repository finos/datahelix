package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.LogicalConstraint;

import java.util.Collection;

public class Rule
{
    public final String description;
    public final Collection<LogicalConstraint> constraints;

    public Rule(String description, Collection<LogicalConstraint> constraints)
    {
        this.description = description;
        this.constraints = constraints;
    }
}
