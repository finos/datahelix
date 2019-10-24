package com.scottlogic.deg.generator.profile.constraints.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;

import java.util.Objects;

public class EqualToConstraint implements AtomicConstraint {

    private final Field field;
    public final Object value;

    public EqualToConstraint(Field field, Object value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AtomicConstraint negate() {
        return new NotEqualToConstraint(field, value);
    }

    @Override
    public FieldSpec toFieldSpec() {
        return FieldSpecFactory.fromList(DistributedList.singleton(value))
            .withNotNull();
    }

    @Override
    public String toString(){
        return String.format("`%s` = %s", field.name, value);
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o instanceof ViolatedAtomicConstraint) {
            return o.equals(this);
        }
        if (o == null || getClass() != o.getClass()) return false;
        EqualToConstraint constraint = (EqualToConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(value, constraint.value);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, value);
    }
}
