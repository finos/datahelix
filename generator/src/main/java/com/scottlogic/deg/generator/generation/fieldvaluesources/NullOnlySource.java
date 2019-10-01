package com.scottlogic.deg.generator.generation.fieldvaluesources;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

public class NullOnlySource implements FieldValueSource {
    private final Set nullOnly = Collections.singleton(null);
    
    @Override
    public Stream<Object> generateInterestingValues() {
        return nullOnly.stream();
    }

    @Override
    public Stream<Object> generateAllValues() {
        return nullOnly.stream();
    }

    @Override
    public Stream<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return Stream.generate(() -> null);
    }
}
