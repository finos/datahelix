package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.inputs.validation.ProfileVisitor;

import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.util.Collection;

public class Rule
{
    public final RuleInformation ruleInformation;
    public final Collection<Constraint> constraints;

    public Rule(RuleInformation ruleInformation, Collection<Constraint> constraints)
    {
        this.ruleInformation = ruleInformation;
        this.constraints = constraints;
    }

}

