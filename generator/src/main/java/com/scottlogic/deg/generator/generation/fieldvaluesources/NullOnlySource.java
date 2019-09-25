package com.scottlogic.deg.generator.generation.fieldvaluesources;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public class NullOnlySource implements FieldValueSource {
    @Override
    public Stream<Object> generateInterestingValues() {
        return generateAllValues();
    }

    @Override
    public Stream<Object> generateAllValues() {
        return generateRandomValues(null)
            .limit(1);
    }

    @Override
    public Stream<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return Stream.generate(() -> null);
    }
}
