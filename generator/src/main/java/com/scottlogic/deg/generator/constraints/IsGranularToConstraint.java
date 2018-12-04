package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.restrictions.ParsedGranularity;

import java.util.Objects;

public class IsGranularToConstraint implements AtomicConstraint {
    public final Field field;
    public final ParsedGranularity granularity;

    public IsGranularToConstraint(Field field, ParsedGranularity granularity) {
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
        if (o == null || getClass() != o.getClass()) return false;
        IsGranularToConstraint constraint = (IsGranularToConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(granularity.getNumericGranularity(), constraint.granularity.getNumericGranularity());
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, granularity.getNumericGranularity());
    }
}
