package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.inputs.validation.ProfileVisitor;
import com.scottlogic.deg.generator.inputs.validation.VisitableProfileElement;
import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.util.Collection;

public class Rule implements VisitableProfileElement
{
    public final RuleInformation rule;
    public final Collection<Constraint> constraints;

    public Rule(RuleInformation rule, Collection<Constraint> constraints)
    {
        this.rule = rule;
        this.constraints = constraints;
    }

    public void accept(ProfileVisitor visitor) {
        constraints.forEach(visitor::visit);
        constraints.stream()
            .filter(f -> f instanceof VisitableProfileElement)
            .map(cc -> (VisitableProfileElement)cc)
            .forEach(cc -> cc.accept(visitor));
    }
}

