package com.scottlogic.deg.generator.utils;

import com.scottlogic.deg.generator.generation.field_value_sources.IFieldValueSource;

public interface IStringGenerator {
    IStringGenerator intersect(IStringGenerator stringGenerator);
    IStringGenerator complement();

    boolean isFinite();
    long getValueCount();
    boolean match(String subject);

    Iterable<String> generateAllValues();

    Iterable<String> generateRandomValues(IRandomNumberGenerator randomNumberGenerator);

    default IFieldValueSource asFieldValueSource() {
        return new StringGeneratorAsFieldValueSource(this);
    }

    // Adapter
    class StringGeneratorAsFieldValueSource implements IFieldValueSource {
        private final IStringGenerator underlyingGenerator;

        StringGeneratorAsFieldValueSource(IStringGenerator underlyingGenerator) {
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
        public Iterable<Object> generateAllValues() {
            return () -> new UpCastingIterator<>(
                underlyingGenerator.generateAllValues().iterator());
        }

        @Override
        public Iterable<Object> generateRandomValues(IRandomNumberGenerator randomNumberGenerator) {
            return () -> new UpCastingIterator<>(
                underlyingGenerator.generateRandomValues(randomNumberGenerator).iterator());
        }
    }
}
