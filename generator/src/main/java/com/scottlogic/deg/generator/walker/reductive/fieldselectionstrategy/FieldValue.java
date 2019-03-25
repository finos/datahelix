package com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy;

import com.scottlogic.deg.generator.Field;

public class FieldValue {
    private final Field field;
    private final Object value;

    public FieldValue(Field field, Object value){
        this.value = value;
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public Field getField() {
        return field;
    }

    @Override
    public String toString() {
        if (value == null) return "null";
        return value.toString();
    }
}
