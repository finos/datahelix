package com.scottlogic.deg.generator.generation.fieldvaluesources;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

public interface FieldValueSource {
    boolean isFinite();
    long getValueCount();

    Iterable<Object> generateInterestingValues();

    Iterable<Object> generateAllValues();

    Iterable<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator);
}

