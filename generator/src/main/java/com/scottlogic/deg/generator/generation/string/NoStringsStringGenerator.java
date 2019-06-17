package com.scottlogic.deg.generator.generation.string;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.Arrays;
import java.util.Collections;

public class NoStringsStringGenerator implements StringGenerator {
    private final String stringRepresentation;

    public NoStringsStringGenerator(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }

    public NoStringsStringGenerator(StringGenerator... generators) {
        this(Arrays
            .stream(generators)
            .map(Object::toString)
            .reduce(RegexStringGenerator::intersectRepresentation)
            .orElseThrow(() -> new IllegalArgumentException("At least one generator must be supplied")));
    }

    @Override
    public String toString() {
        return String.format("No strings: %s", this.stringRepresentation);
    }

    @Override
    public StringGenerator intersect(StringGenerator stringGenerator) {
        return new NoStringsStringGenerator(
            RegexStringGenerator.intersectRepresentation(this.stringRepresentation, stringGenerator.toString()));
    }

    @Override
    public StringGenerator complement() {
        throw new RuntimeException("Not implemented: Return a string generator able to emit ALL strings");
    }

    @Override
    public boolean isFinite() {
        return true;
    }

    @Override
    public long getValueCount() {
        return 0;
    }

    @Override
    public boolean match(String subject) {
        return false;
    }

    @Override
    public Iterable<String> generateInterestingValues() {
        return Collections.emptySet();
    }

    @Override
    public Iterable<String> generateAllValues() {
        return Collections.emptySet();
    }

    @Override
    public Iterable<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return Collections.emptySet();
    }
}
