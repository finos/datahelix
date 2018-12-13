package com.scottlogic.deg.generator.generation.field_value_sources;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

public interface FieldValueSource {
    boolean isFinite();
    long getValueCount();

    Iterable<Object> generateInterestingValues();

    Iterable<Object> generateAllValues();

    Iterable<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator);
}

