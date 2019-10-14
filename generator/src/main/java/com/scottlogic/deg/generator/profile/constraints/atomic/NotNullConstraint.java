package com.scottlogic.deg.generator.profile.constraints.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;

import java.util.Objects;

public class NotNullConstraint implements AtomicConstraint {
    public final Field field;

    public NotNullConstraint(Field field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return String.format("%s is null", field.name);
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AtomicConstraint negate() {
        return new IsNullConstraint(field);
    }

    @Override
    public FieldSpec toFieldSpec() {
        return FieldSpecFactory.fromType(field.getType()).withNotNull();
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o instanceof ViolatedAtomicConstraint) {
            return o.equals(this);
        }
        if (o == null || getClass() != o.getClass()) return false;
        NotNullConstraint constraint = (NotNullConstraint) o;
        return Objects.equals(field, constraint.field);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field);
    }

}
