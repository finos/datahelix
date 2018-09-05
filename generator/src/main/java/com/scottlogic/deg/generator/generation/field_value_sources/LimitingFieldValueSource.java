package com.scottlogic.deg.generator.generation.field_value_sources;

import com.scottlogic.deg.generator.utils.IRandomNumberGenerator;
import com.scottlogic.deg.generator.utils.LimitingIterable;

public class LimitingFieldValueSource implements IFieldValueSource {
    private final IFieldValueSource underlyingSource;
    private final int minimumCount;
    private final int maximumCount;

    public LimitingFieldValueSource(
        IFieldValueSource underlyingSource,
        int minimumCount,
        int maximumCount) {
        this.underlyingSource = underlyingSource;
        this.minimumCount = minimumCount;
        this.maximumCount = maximumCount;
    }

    @Override
    public boolean isFinite() {
        return true;
    }

    @Override
    public long getValueCount() {
        if (!underlyingSource.isFinite()) {
            return this.minimumCount;
        }

        if (underlyingSource.getValueCount() > this.maximumCount) {
            return this.maximumCount;
        }

        return underlyingSource.getValueCount();
    }

    @Override
    public Iterable<Object> generateInterestingValues() {
        return new LimitingIterable<>(
            underlyingSource.generateInterestingValues(),
            this.getValueCount());
    }

    @Override
    public Iterable<Object> generateAllValues() {
        return new LimitingIterable<>(
            underlyingSource.generateAllValues(),
            this.getValueCount());
    }

    @Override
    public Iterable<Object> generateRandomValues(IRandomNumberGenerator randomNumberGenerator) {
        return new LimitingIterable<>(
            underlyingSource.generateRandomValues(randomNumberGenerator),
            this.getValueCount());
    }
}
