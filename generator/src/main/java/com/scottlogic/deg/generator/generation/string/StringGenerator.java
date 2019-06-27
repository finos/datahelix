package com.scottlogic.deg.generator.generation.string;

import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import com.scottlogic.deg.generator.utils.UpCastingIterator;

public interface StringGenerator {
    StringGenerator intersect(StringGenerator stringGenerator);
    StringGenerator complement();

    boolean isFinite();
    long getValueCount();
    boolean match(String subject);

    Iterable<String> generateInterestingValues();

    Iterable<String> generateAllValues();

    Iterable<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator);

    default FieldValueSource asFieldValueSource() {
        return new StringGeneratorAsFieldValueSource(this);
    }

    // Adapter
    class StringGeneratorAsFieldValueSource implements FieldValueSource {
        private final StringGenerator underlyingGenerator;

        StringGeneratorAsFieldValueSource(StringGenerator underlyingGenerator) {
            this.underlyingGenerator = underlyingGenerator;
        }

        @Override
        public boolean isFinite() {
            return underlyingGenerator.isFinite();
        }

        @Override
        public long getValueCount() {
            return underlyingGenerator.getValueCount();
        }

        @Override
        public Iterable<Object> generateInterestingValues() {
            return () -> new UpCastingIterator<>(
                underlyingGenerator.generateInterestingValues().iterator());
        }

        @Override
        public Iterable<Object> generateAllValues() {
            return () -> new UpCastingIterator<>(
                underlyingGenerator.generateAllValues().iterator());
        }

        @Override
        public Iterable<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
            return () -> new UpCastingIterator<>(
                underlyingGenerator.generateRandomValues(randomNumberGenerator).iterator());
        }
    }
}
