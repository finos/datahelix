package com.scottlogic.deg.profile.custom;

import com.scottlogic.deg.custom.CustomGenerator;
import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.stream.Stream;

public class CustomFieldValueSource<T> implements FieldValueSource<T> {
    private final CustomGenerator customGenerator;
    private final boolean negated;

    public CustomFieldValueSource(CustomGenerator customGenerator, boolean negated) {
        this.customGenerator = customGenerator;
        this.negated = negated;
    }

    @Override
    public Stream<T> generateInterestingValues() {
        return generateAllValues().limit(2);
    }

    @Override
    public Stream<T> generateAllValues() {
        if (negated){
            return customGenerator.generateNegatedSequential();
        }
        return customGenerator.generateSequential();
    }

    @Override
    public Stream<T> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        if (negated){
            return customGenerator.generateNegatedRandom();
        }
        return customGenerator.generateRandom();
    }
}
