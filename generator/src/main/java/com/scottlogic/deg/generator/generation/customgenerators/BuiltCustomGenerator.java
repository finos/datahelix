package com.scottlogic.deg.generator.generation.customgenerators;

import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BuiltCustomGenerator<T> implements CustomGenerator<T> {

    private final FieldType fieldType;
    private final String name;
    private final Function<T, Boolean> matchingFunction;
    private final Function<RandomNumberGenerator, Stream<T>> randomGenerator;
    private final Function<RandomNumberGenerator, Stream<T>>  negatedRandomGenerator;
    private final Supplier<Stream<T>> sequentialGenerator;
    private final Supplier<Stream<T>> negatedSequentialGenerator;

    public BuiltCustomGenerator(FieldType fieldType,
                                String name,
                                Function<T, Boolean> matchingFunction,
                                Function<RandomNumberGenerator, Stream<T>> randomGenerator,
                                Function<RandomNumberGenerator, Stream<T>>  negatedRandomGenerator,
                                Supplier<Stream<T>> sequentialGenerator,
                                Supplier<Stream<T>> negatedSequentialGenerator) {
        this.fieldType = fieldType;
        this.name = name;
        this.matchingFunction = matchingFunction;
        this.randomGenerator = randomGenerator;
        this.negatedRandomGenerator = negatedRandomGenerator;
        this.sequentialGenerator = sequentialGenerator;
        this.negatedSequentialGenerator = negatedSequentialGenerator;
    }

    @Override
    public String generatorName() {
        return name;
    }

    @Override
    public FieldType fieldType() {
        return fieldType;
    }

    @Override
    public Stream<T> generateRandom(RandomNumberGenerator randomNumberGenerator) {
        return randomGenerator.apply(randomNumberGenerator);
    }

    @Override
    public Stream<T> generateNegatedRandom(RandomNumberGenerator randomNumberGenerator) {
        return negatedRandomGenerator.apply(randomNumberGenerator);
    }

    @Override
    public Stream<T> generateSequential() {
        return sequentialGenerator.get();
    }

    @Override
    public Stream<T> generateNegatedSequential() {
        return negatedSequentialGenerator.get();
    }

    @Override
    public boolean setMatchingFunction(T value) {
        return matchingFunction.apply(value);
    }
}
