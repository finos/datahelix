package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.Value;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;

import java.util.HashMap;
import java.util.Map;

public class GeneratedObjectBuilder {
    private final Map<Field, Value> fieldToValue;
    private GeneratedObjectBuilder() {
        this.fieldToValue = new HashMap<>();
    }

    public static GeneratedObjectBuilder startBuilding() { return new GeneratedObjectBuilder(); }

    public GeneratedObjectBuilder set(Field field, Value value) {
        if (this.fieldToValue.containsKey(field))
            throw new IllegalArgumentException("Databag already contains a value for " + field);

        this.fieldToValue.put(field, value);

        return this;
    }

    public GeneratedObjectBuilder set(Field field, Object value, FieldSpecSource source) {
        return this.set(field, new Value(field, value, null, source));
    }

    public Row build() {
        return new Row(this.fieldToValue);
    }
}

