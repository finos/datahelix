package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.util.FlatMappingSpliterator;
import com.scottlogic.deg.common.output.GeneratedObject;

import java.util.*;


public class DataBag implements GeneratedObject {
    public static final DataBag empty = new DataBag(new HashMap<>());

    private final Map<Field, DataBagValue> fieldToValue;

    public DataBag(Map<Field, DataBagValue> fieldToValue) {
        this.fieldToValue = fieldToValue;
    }

    public DataBagValue getDataBagValue(Field field) {
        if (!fieldToValue.containsKey(field))
            throw new IllegalStateException("Databag has no value stored for " + field);

        return fieldToValue.get(field);
    }

    @Override
    public Object getFormattedValue(Field field) {
        if (!fieldToValue.containsKey(field))
            throw new IllegalStateException("DataBag has no value stored for " + field);

        return fieldToValue.get(field).getFormattedValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataBag generatedObject = (DataBag) o;
        return Objects.equals(fieldToValue, generatedObject.fieldToValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldToValue);
    }

    public static DataBag merge(DataBag... bags) {
        Map<Field, DataBagValue> newFieldToValue = new HashMap<>();

        FlatMappingSpliterator.flatMap(Arrays.stream(bags)
            .map(r -> r.fieldToValue.entrySet().stream()),
            entrySetStream -> entrySetStream)
            .forEach(entry -> {
                if (newFieldToValue.containsKey(entry.getKey()))
                    throw new IllegalArgumentException("Databags can't be merged because they overlap on field " + entry.getKey().name);

                newFieldToValue.put(entry.getKey(), entry.getValue());
            });

        return new DataBag(newFieldToValue);
    }
}
