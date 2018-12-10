package com.scottlogic.deg.generator.constraints.grammatical;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.constraints.ConstraintRule;

import java.util.Collection;

public class ViolateConstraint implements GrammaticalConstraint {
    public final Constraint violatedConstraint;
    private final ConstraintRule rule;

    public ViolateConstraint(Constraint violatedConstraint) {
        this.violatedConstraint = violatedConstraint;
        this.rule = violatedConstraint.getRule().violate();
    }

    @Override
    public Collection<Field> getFields() {
        return violatedConstraint.getFields();
    }

    @Override
    public ConstraintRule getRule() {
        return this.rule;
    }

    @Override
    public String toString() {
        return String.format("VIOLATE: %s", violatedConstraint.toString());
    }
}
