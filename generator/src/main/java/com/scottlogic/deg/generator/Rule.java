package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.inputs.validation.ProfileVisitor;
import com.scottlogic.deg.generator.inputs.validation.VisitableProfileElement;

import java.util.Collection;

public class Rule implements VisitableProfileElement
{
    public final String description;
    public final Collection<Constraint> constraints;

    public Rule(String description, Collection<Constraint> constraints)
    {
        this.description = description;
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

