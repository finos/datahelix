package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.*;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;
import com.scottlogic.deg.generator.outputs.CellSource;
import com.scottlogic.deg.generator.outputs.RowSource;

import java.util.*;
import java.util.stream.Collectors;


public class GeneratedObject {
    public static final GeneratedObject empty = new GeneratedObject(new HashMap<>());

    private final Map<Field, DataBagValue> fieldToValue;
    private final ProfileFields fieldOrdering;

    public GeneratedObject(Map<Field, DataBagValue> fieldToValue) {
        this(fieldToValue, null);
    }

    private GeneratedObject(Map<Field, DataBagValue> fieldToValue, ProfileFields fieldOrdering){
        this.fieldToValue = fieldToValue;
        this.fieldOrdering = fieldOrdering;
    }

    public Collection<DataBagValue> getValues() {
        if (fieldOrdering == null) {
            return fieldToValue.values();
        }

        return fieldOrdering.stream()
            .map(fieldToValue::get)
            .collect(Collectors.toList());
    }

    public GeneratedObject withOrdering(ProfileFields fieldOrdering){
        return new GeneratedObject(fieldToValue, fieldOrdering);
    }

    public Object getValue(Field field) {
        if (!this.fieldToValue.containsKey(field))
            throw new IllegalStateException("Databag has no value stored for " + field);

        return this.fieldToValue.get(field).value;
    }

    public DataBagValue getValueAndFormat(Field field) {
        if (!this.fieldToValue.containsKey(field))
            throw new IllegalStateException("Databag has no value stored for " + field);

        return this.fieldToValue.get(field);
    }

    public static GeneratedObject merge(GeneratedObject... bags) {
        Map<Field, DataBagValue> newFieldToValue = new HashMap<>();

        FlatMappingSpliterator.flatMap(Arrays.stream(bags)
            .map(r -> r.fieldToValue.entrySet().stream()),
            entrySetStream -> entrySetStream)
            .forEach(entry -> {
                if (newFieldToValue.containsKey(entry.getKey()))
                    throw new IllegalArgumentException("Databags can't be merged because they overlap on field " + entry.getKey().name);

                newFieldToValue.put(entry.getKey(), entry.getValue());
            });

        return new GeneratedObject(newFieldToValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeneratedObject generatedObject = (GeneratedObject) o;
        return Objects.equals(fieldToValue, generatedObject.fieldToValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldToValue);
    }

    public RowSource getRowSource() {
        return new RowSource(
            fieldToValue.keySet()
                .stream()
                .map(field -> {
                    DataBagValue value = this.fieldToValue.get(field);
                    return new CellSource(value, field);
                })
                .collect(Collectors.toList())
        );
    }
}
