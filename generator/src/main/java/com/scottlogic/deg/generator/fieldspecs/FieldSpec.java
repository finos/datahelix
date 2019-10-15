package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;

import java.util.Set;

public interface FieldSpec {
    abstract boolean permits(Object value);
    abstract FieldValueSource getFieldValueSource();

    FieldSpec withNotNull();

    boolean isNullable();
}
