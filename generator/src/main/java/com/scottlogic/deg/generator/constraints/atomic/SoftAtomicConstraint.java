package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.util.Set;

public class SoftAtomicConstraint implements AtomicConstraint {
    public final AtomicConstraint underlyingConstraint;

    public SoftAtomicConstraint(AtomicConstraint underlyingConstraint) {
        this.underlyingConstraint = underlyingConstraint;
    }

    @Override
    public Field getField() {
        return underlyingConstraint.getField();
    }

    @Override
    public String toDotLabel() {
        return underlyingConstraint.toDotLabel();
    }

    @Override
    public AtomicConstraint withRules(Set<RuleInformation> rules) {
        return new SoftAtomicConstraint(underlyingConstraint.withRules(rules));
    }

    @Override
    public Set<RuleInformation> getRules() {
        return underlyingConstraint.getRules();
    }

    @Override
    public String toString() {
        return String.format("soft(%s)", underlyingConstraint.toString());
    }
}
