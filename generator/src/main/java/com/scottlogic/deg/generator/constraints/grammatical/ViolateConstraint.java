package com.scottlogic.deg.generator.constraints.grammatical;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.util.Collection;

public class ViolateConstraint implements GrammaticalConstraint {
    public final Constraint violatedConstraint;

    public ViolateConstraint(Constraint violatedConstraint) {
        this.violatedConstraint = violatedConstraint;
    }

    @Override
    public Collection<Field> getFields() {
        return violatedConstraint.getFields();
    }

    @Override
    public RuleInformation getRule() {
        return this.violatedConstraint.getRule();
    }

    @Override
    public String toString() {
        return String.format("VIOLATE: %s", violatedConstraint.toString());
    }
}
