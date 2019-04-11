package com.scottlogic.deg.generator.generation.rows;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;

import java.util.HashMap;
import java.util.Map;

public class RowBuilder {
    private final Map<Field, Value> fieldToValue;
    private RowBuilder() {
        this.fieldToValue = new HashMap<>();
    }

    public static RowBuilder startBuilding() { return new RowBuilder(); }

    public RowBuilder set(Field field, Value value) {
        if (this.fieldToValue.containsKey(field))
            throw new IllegalArgumentException("Row already contains a value for " + field);

        this.fieldToValue.put(field, value);

        return this;
    }

    public RowBuilder set(Field field, Object value, FieldSpecSource source) {
        return this.set(field, new Value(field, value, null, source));
    }

    public Row build() {
        return new Row(this.fieldToValue);
    }
}

