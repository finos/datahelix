package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.ConstraintRule;
import com.scottlogic.deg.generator.restrictions.ParsedGranularity;

import java.util.Objects;

public class IsGranularToConstraint implements AtomicConstraint {
    public final Field field;
    private final ConstraintRule rule;
    public final ParsedGranularity granularity;

    public IsGranularToConstraint(Field field, ParsedGranularity granularity, ConstraintRule rule) {
        this.granularity = granularity;
        this.field = field;
        this.rule = rule;
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

    @Override
    public ConstraintRule getRule() {
        return rule;
    }
}
