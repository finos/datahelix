package com.scottlogic.deg.common.profile.constraints.atomic;

import com.scottlogic.deg.common.profile.constraintdetail.ParsedGranularity;
import com.scottlogic.deg.common.profile.Field;

import com.scottlogic.deg.common.profile.RuleInformation;

import java.util.Objects;
import java.util.Set;

public class IsGranularToNumericConstraint implements AtomicConstraint {
    public final Field field;
    public final ParsedGranularity granularity;

    public IsGranularToNumericConstraint(Field field, ParsedGranularity granularity) {
        if(field == null)
            throw new IllegalArgumentException("field must not be null");
        if(granularity == null)
            throw new IllegalArgumentException("granularity must not be null");

        this.granularity = granularity;
        this.field = field;
    }

    @Override
    public String toDotLabel() {
        return String.format("%s granular to %s", field.name, granularity.getNumericGranularity());
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
        IsGranularToNumericConstraint constraint = (IsGranularToNumericConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(granularity.getNumericGranularity(), constraint.granularity.getNumericGranularity());
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, granularity.getNumericGranularity());
    }

    @Override
    public String toString() {
        return String.format("granularTo %s", this.granularity.getNumericGranularity());
    }
}
