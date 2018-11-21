package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class StringHasLengthConstraint implements IConstraint {
    public final Field field;
    public final Number referenceValue;

    public StringHasLengthConstraint(Field field, Number referenceValue) {
        if (referenceValue == null) {
            throw new IllegalArgumentException("Argument 'referenceValue' cannot be null.");
        }

        this.referenceValue = referenceValue;
        this.field = field;
    }

    @Override
    public String toDotLabel() {
        return String.format("%s length = %s", field.name, referenceValue);
    }

    @Override
    public Collection<Field> getFields() {
        return Collections.singletonList(field);
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringHasLengthConstraint constraint = (StringHasLengthConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(referenceValue, constraint.referenceValue);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, referenceValue);
    }

    @Override
    public String toString() { return String.format("`%s` length = %s", field.name, referenceValue); }
}
