package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.ConstraintRule;

import java.time.LocalDateTime;
import java.util.Objects;

public class IsBeforeConstantDateTimeConstraint implements AtomicConstraint {
    public final Field field;
    public final LocalDateTime referenceValue;
    private final ConstraintRule rule;

    public IsBeforeConstantDateTimeConstraint(Field field, LocalDateTime referenceValue, ConstraintRule rule) {
        this.field = field;
        this.referenceValue = referenceValue;
        this.rule = rule;
    }

    @Override
    public String toDotLabel() {
        return String.format("%s < %s", field.name, referenceValue);
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IsBeforeConstantDateTimeConstraint constraint = (IsBeforeConstantDateTimeConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(referenceValue, constraint.referenceValue);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, referenceValue);
    }

    @Override
    public String toString() {
        return String.format("`%s` < %s", field.name, referenceValue);
    }

    @Override
    public ConstraintRule getRule() {
        return rule;
    }
}
