package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.util.Objects;
import java.util.Set;

public class IsEqualToConstantConstraint implements AtomicConstraint {
    public final Field field;
    public final Object requiredValue;
    private final Set<RuleInformation> rules;

    public IsEqualToConstantConstraint(Field field, Object requiredValue, Set<RuleInformation> rules) {
        if (requiredValue == null){
            throw new IllegalArgumentException("Cannot create an IsEqualToConstantConstraint for field '" +
                field.name + "' with the null value.");
        }

        this.field = field;
        this.requiredValue = requiredValue;
        this.rules = rules;
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
    public Set<RuleInformation> getRules() {
        return rules;
    }

    @Override
    public AtomicConstraint withRules(Set<RuleInformation> rules) {
        return new IsEqualToConstantConstraint(this.field, this.requiredValue, rules);
    }
}
