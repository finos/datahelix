package com.scottlogic.deg.generator.generation.field_value_sources;

import com.scottlogic.deg.generator.utils.IRandomNumberGenerator;

public interface IFieldValueSource {
    boolean isFinite();
    long getValueCount();

    Iterable<Object> generateBoundaryValues();

    Iterable<Object> generateAllValues();

    Iterable<Object> generateRandomValues(IRandomNumberGenerator randomNumberGenerator);
}

