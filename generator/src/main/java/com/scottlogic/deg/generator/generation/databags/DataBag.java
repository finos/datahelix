package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.Field;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DataBag {
    private final Map<Field, Object> fieldToValue;

    public DataBag() {
        this(new HashMap<>());
    }

    private DataBag(Map<Field, Object> fieldToValue) {
        this.fieldToValue = fieldToValue;
    }

    public Object get(Field field) {
        if (!this.fieldToValue.containsKey(field))
            throw new IllegalStateException("Databag has no value stored for " + field);

        return this.fieldToValue.get(field);
    }

    public void set(Field field, Object value) {
        if (this.fieldToValue.containsKey(field))
            throw new IllegalArgumentException("Databag already contains a value for " + field);

        this.fieldToValue.put(field, value);
    }

    public static DataBag merge(DataBag... bags) {
        Map<Field, Object> newMap =
            Arrays.stream(bags)
                .map(r -> r.fieldToValue.entrySet().stream())
                .flatMap(entrySetStream -> entrySetStream)
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue));

        return new DataBag(newMap);
    }
}
