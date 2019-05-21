package com.scottlogic.deg.common.output;

import com.scottlogic.deg.common.profile.Field;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/** A set of values representing one complete, discrete output (eg, this could be used to make a full CSV row) */
public class GeneratedObject {

    private final Map<Field, DataBagValue> fieldToValue;

    public GeneratedObject(Map<Field, DataBagValue> fieldToValue) {
        this.fieldToValue = fieldToValue;
    }

    public Map<Field, DataBagValue> getFieldToValue() {
        return fieldToValue;
    }

    public Object getValue(Field field) {
        if (!this.fieldToValue.containsKey(field))
            throw new IllegalStateException("Databag has no value stored for " + field);

        return this.fieldToValue.get(field).getUnformattedValue();
    }

    public DataBagValue getValueAndFormat(Field field) {
        if (!fieldToValue.containsKey(field))
            throw new IllegalStateException("GeneratedObject has no value stored for " + field);

        return fieldToValue.get(field);
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
            fieldToValue.entrySet().stream()
                .map(e -> new CellSource(e.getKey() , e.getValue()))
                .collect(Collectors.toList())
        );
    }
}
