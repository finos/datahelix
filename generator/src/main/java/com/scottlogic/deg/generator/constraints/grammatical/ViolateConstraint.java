package com.scottlogic.deg.generator.constraints.grammatical;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.Constraint;
import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.util.Collection;
import java.util.Set;

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
    public Set<RuleInformation> getRules() {
        return this.violatedConstraint.getRules();
    }

    @Override
    public String toString() {
        return String.format("VIOLATE: %s", violatedConstraint.toString());
    }
}
