package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

public class IsAfterConstantDateTimeConstraint implements AtomicConstraint {
    public final Field field;
    public final LocalDateTime referenceValue;
    private final Set<RuleInformation> rules;

    public IsAfterConstantDateTimeConstraint(Field field, LocalDateTime referenceValue, Set<RuleInformation> rules) {
        this.field = field;
        this.referenceValue = referenceValue;
        this.rules = rules;
    }

    @Override
    public String toDotLabel(){
        return String.format("%s > %s", field.name, referenceValue);
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
        IsAfterConstantDateTimeConstraint constraint = (IsAfterConstantDateTimeConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(referenceValue, constraint.referenceValue);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, referenceValue);
    }

    @Override
    public String toString(){
        return String.format("`%s` > %s", field.name, referenceValue);
    }

    @Override
    public Set<RuleInformation> getRules() {
        return rules;
    }
}
