package com.scottlogic.deg.generator.generation.rows;

import com.scottlogic.deg.generator.*;

import java.util.*;


public class Row {
    public static final Row empty = new Row(new HashMap<>());

    private final Map<Field, Value> fieldToValue;

    public Row(Map<Field, Value> fieldToValue) {
        this.fieldToValue = fieldToValue;
    }

    public Map<Field, Value> getFieldToValue() {
        return fieldToValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Row row = (Row) o;
        return Objects.equals(fieldToValue, row.fieldToValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldToValue);
    }
}
