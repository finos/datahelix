package com.scottlogic.deg.generator.analysis;

import com.scottlogic.deg.generator.Field;

public class FieldDependency {

    private final Field field;
    private final int dependencyLevel;

    public FieldDependency(Field field, int dependencyLevel) {
        this.field = field;
        this.dependencyLevel = dependencyLevel;
    }

    public Field getField() {
        return field;
    }

    public int getDependencyLevel() {
        return dependencyLevel;
    }
}
