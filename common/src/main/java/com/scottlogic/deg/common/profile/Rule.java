package com.scottlogic.deg.common.profile;

import com.scottlogic.deg.common.constraint.Constraint;

import com.scottlogic.deg.common.profile.RuleInformation;

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

