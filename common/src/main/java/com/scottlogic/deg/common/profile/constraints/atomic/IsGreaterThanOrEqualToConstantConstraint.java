package com.scottlogic.deg.common.profile.constraints.atomic;

import com.scottlogic.deg.common.profile.Field;

import com.scottlogic.deg.common.profile.RuleInformation;

import java.util.Objects;
import java.util.Set;

public class IsGreaterThanOrEqualToConstantConstraint implements AtomicConstraint {
    public final Field field;
    public final Number referenceValue;

    public IsGreaterThanOrEqualToConstantConstraint(Field field, Number referenceValue) {
        this.referenceValue = referenceValue;
        this.field = field;
    }

    @Override
    public String toDotLabel() {
        return String.format("%s >= %s", field.name, referenceValue);
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
        IsGreaterThanOrEqualToConstantConstraint constraint = (IsGreaterThanOrEqualToConstantConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(referenceValue, constraint.referenceValue);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, referenceValue);
    }

    @Override
    public String toString() {
        return String.format("`%s` >= %s", field.name, referenceValue);
    }
}
