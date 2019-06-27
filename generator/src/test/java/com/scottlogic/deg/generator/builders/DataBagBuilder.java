package com.scottlogic.deg.generator.builders;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.generation.databags.DataBagValue;

import java.util.HashMap;
import java.util.Map;

public class DataBagBuilder {
    private final Map<Field, DataBagValue> fieldToValue;

    public DataBagBuilder() {
        fieldToValue = new HashMap<>();
    }

    public DataBagBuilder set(Field field, DataBagValue value) {
        if (fieldToValue.containsKey(field)) {
            throw new IllegalArgumentException("Databag already contains a value for " + field);
        }
        fieldToValue.put(field, value);

        return this;
    }

    public DataBagBuilder set(Field field, Object value) {
        return this.set(field, new DataBagValue(value));
    }

    public DataBag build() {
        return new DataBag(fieldToValue);
    }
}
