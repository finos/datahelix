package com.scottlogic.deg.generator.generation.combination;


import com.scottlogic.deg.generator.fieldspecs.FieldSpec;

import java.util.Objects;

public final class DataValue {

    private Object value;
    private FieldSpec source;

    public DataValue(Object value, FieldSpec source) {
        this.value = value;
        this.source = source;
    }

    public Object getValue() {
        return value;
    }

    public FieldSpec getSource() {
        return source;
    }

    public String toString(){
        return value == null ? "null" : value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataValue dataValue = (DataValue) o;
        return Objects.equals(value, dataValue.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
