package com.scottlogic.deg.common.profile.constraints.atomic;

import com.scottlogic.deg.common.profile.Field;

import java.time.OffsetDateTime;
import java.util.Objects;

public class IsAfterConstantDateTimeConstraint implements AtomicConstraint {
    public final Field field;
    public final OffsetDateTime referenceValue;

    public IsAfterConstantDateTimeConstraint(Field field, OffsetDateTime referenceValue) {
        this.field = field;
        this.referenceValue = referenceValue;
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
}
