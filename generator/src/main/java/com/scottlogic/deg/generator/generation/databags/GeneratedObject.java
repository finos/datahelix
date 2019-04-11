package com.scottlogic.deg.generator.generation.databags;

import com.scottlogic.deg.generator.*;

import java.util.*;


public class GeneratedObject {
    public static final GeneratedObject empty = new GeneratedObject(new HashMap<>());

    private final Map<Field, Value> fieldToValue;

    public GeneratedObject(Map<Field, Value> fieldToValue) {
        this.fieldToValue = fieldToValue;
    }

    public Map<Field, Value> getFieldToValue() {
        return fieldToValue;
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
}
