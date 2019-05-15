package com.scottlogic.deg.common.profile;

import com.scottlogic.deg.common.profile.constraints.Constraint;

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

