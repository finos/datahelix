package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.util.Collection;

public class Rule
{
    public final RuleInformation rule;
    public final Collection<Constraint> constraints;

    public Rule(RuleInformation rule, Collection<Constraint> constraints)
    {
        this.rule = rule;
        this.constraints = constraints;
    }
}
