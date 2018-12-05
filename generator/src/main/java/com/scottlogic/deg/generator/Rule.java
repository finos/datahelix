package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.Constraint;

import java.util.Collection;

public class Rule
{
    public final String description;
    public final Collection<Constraint> constraints;

    public Rule(String description, Collection<Constraint> constraints)
    {
        this.description = description;
        this.constraints = constraints;
    }
}
