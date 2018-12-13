package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.util.Objects;

public class StringHasLengthConstraint implements AtomicConstraint {
    public final Field field;
    public final Number referenceValue;
    private final RuleInformation rule;

    public StringHasLengthConstraint(Field field, Number referenceValue, RuleInformation rule) {
        this.rule = rule;
        if (referenceValue == null) {
            throw new IllegalArgumentException("Argument 'referenceValue' cannot be null.");
        }

        this.referenceValue = referenceValue;
        this.field = field;
    }

    @Override
    public String toDotLabel() {
        return String.format("%s length = %s", field.name, referenceValue);
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o instanceof ViolatedAtomicConstraint) {
            return o.equals(this);
        }
        if (o == null || getClass() != o.getClass()) return false;
        StringHasLengthConstraint constraint = (StringHasLengthConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(referenceValue, constraint.referenceValue);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, referenceValue);
    }

    @Override
    public String toString() { return String.format("`%s` length = %s", field.name, referenceValue); }

    @Override
    public RuleInformation getRule() {
        return rule;
    }
}
