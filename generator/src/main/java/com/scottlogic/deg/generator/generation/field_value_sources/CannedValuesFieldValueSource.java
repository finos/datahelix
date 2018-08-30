package com.scottlogic.deg.generator.generation.field_value_sources;

import com.scottlogic.deg.generator.utils.IRandomNumberGenerator;
import com.scottlogic.deg.generator.utils.SupplierBasedIterator;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CannedValuesFieldValueSource implements IFieldValueSource {
    private final List<Object> values;

    public CannedValuesFieldValueSource(List<Object> values) {
        this.values = values;
    }

    public static IFieldValueSource of(Object... values) {
        return new CannedValuesFieldValueSource(Arrays.asList(values));
    }

    @Override
    public boolean isFinite() {
        return true;
    }

    @Override
    public long getValueCount() {
        return this.values.size();
    }

    @Override
    public Iterable<Object> generateBoundaryValues() {
        return () -> Stream.of(
                values.get(0),
                values.get(values.size() / 2),
                values.get(values.size() - 1))
            .distinct()
            .iterator();
    }

    @Override
    public Iterable<Object> generateAllValues() {
        return values;
    }

    @Override
    public Iterable<Object> generateRandomValues(IRandomNumberGenerator randomNumberGenerator) {
        return () -> new SupplierBasedIterator<>(
            () -> values.get(randomNumberGenerator.nextInt(values.size())));
    }
}
