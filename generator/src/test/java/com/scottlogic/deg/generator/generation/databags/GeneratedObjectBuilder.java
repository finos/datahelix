package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.DataBagValue;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;

import java.util.HashMap;
import java.util.Map;

public class GeneratedObjectBuilder {
    private final Map<Field, DataBagValue> fieldToValue;
    private GeneratedObjectBuilder() {
        this.fieldToValue = new HashMap<>();
    }

    public static GeneratedObjectBuilder startBuilding() { return new GeneratedObjectBuilder(); }

    public GeneratedObjectBuilder set(Field field, DataBagValue value) {
        if (this.fieldToValue.containsKey(field))
            throw new IllegalArgumentException("Databag already contains a value for " + field);

        this.fieldToValue.put(field, value);

        return this;
    }

    public GeneratedObjectBuilder set(Field field, Object value, FieldSpecSource source) {
        return this.set(field, new DataBagValue(field, value, null, source));
    }

    public GeneratedObject build() {
        return new GeneratedObject(this.fieldToValue);
    }
}

