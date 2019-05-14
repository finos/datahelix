package com.scottlogic.deg.common.constraint.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.RuleInformation;

import java.util.Collection;
import java.util.Set;

public class ViolatedAtomicConstraint implements AtomicConstraint {
    public final AtomicConstraint violatedConstraint;

    public ViolatedAtomicConstraint(AtomicConstraint violatedConstraint) {
        this.violatedConstraint = violatedConstraint;
    }

    @Override
    public Field getField() {
        return violatedConstraint.getField();
    }

    @Override
    public String toDotLabel() {
        return "Violated: " + violatedConstraint.toDotLabel();
    }

    @Override
    public AtomicConstraint negate() {
        return new ViolatedAtomicConstraint(violatedConstraint.negate());
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
        return String.format("Violated: %s", this.violatedConstraint.toString());
    }

    public int hashCode(){
        return violatedConstraint.hashCode();
    }

    public boolean equals(Object obj){
        return violatedConstraint.equals(obj);
    }

    @Override
    public AtomicConstraint withRules(Set<RuleInformation> rules) {
        return new ViolatedAtomicConstraint(this.violatedConstraint.withRules(rules));
    }
}
