package com.scottlogic.deg.generator.constraints.atomic;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.ConstraintRule;
import com.scottlogic.deg.generator.restrictions.NullRestrictions;

import java.util.Objects;

public class IsNullConstraint implements AtomicConstraint
{
    public final Field field;
    private final ConstraintRule rule;

    public IsNullConstraint(Field field, ConstraintRule rule) {
        this.field = field;
        this.rule = rule;
    }

    @Override
    public String toDotLabel() {
        return String.format("%s is null", field.name);
    }

    public String toString(){
        return String.format(
                "`%s`: %s",
                NullRestrictions.Nullness.MUST_BE_NULL,
                field.toString());
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
        IsNullConstraint constraint = (IsNullConstraint) o;
        return Objects.equals(field, constraint.field);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field);
    }

    @Override
    public ConstraintRule getRule() {
        return rule;
    }
}
