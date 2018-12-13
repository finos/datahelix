package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.util.Objects;

public class IsEqualToConstantConstraint implements AtomicConstraint {
    public final Field field;
    public final Object requiredValue;
    private final RuleInformation rule;

    public IsEqualToConstantConstraint(Field field, Object requiredValue, RuleInformation rule) {
        this.field = field;
        this.requiredValue = requiredValue;
        this.rule = rule;
    }

    @Override
    public String toDotLabel() {
        return String.format("%s = %s", field.name, requiredValue);
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
        IsEqualToConstantConstraint constraint = (IsEqualToConstantConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(requiredValue, constraint.requiredValue);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, requiredValue);
    }

    @Override
    public String toString() {
        return String.format("`%s` = %s", field.name, requiredValue);
    }

    @Override
    public RuleInformation getRule() {
        return rule;
    }
}
