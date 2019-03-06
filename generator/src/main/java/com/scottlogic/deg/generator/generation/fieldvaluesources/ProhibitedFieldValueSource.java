package com.scottlogic.deg.generator.generation.fieldvaluesources;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.*;
import java.util.stream.StreamSupport;

/**
 * A FieldValueSource capable of emitting values from its underlying source that have
 * not been previously prohibited.
 *
 * Calling prohibitValue will record the value as being prohibited to ensure it is not re-emitted
 * when this source is asked for its values.
 */
public class ProhibitedFieldValueSource implements FieldValueSource {
    private final FieldValueSource underlyingSource;
    private final ArrayList<Object> prohibitedValues = new ArrayList<>();

    public ProhibitedFieldValueSource(FieldValueSource underlyingSource) {
        this.underlyingSource = underlyingSource;
    }

    @Override
    public boolean isFinite() {
        return underlyingSource.isFinite();
    }

    @Override
    public long getValueCount() {
        return underlyingSource.getValueCount(); //cannot be relied on
    }

    @Override
    public Iterable<Object> generateInterestingValues() {
        return removeProhibitedValues(underlyingSource.generateInterestingValues());
    }

    @Override
    public Iterable<Object> generateAllValues() {
        return removeProhibitedValues(underlyingSource.generateAllValues());
    }

    @Override
    public Iterable<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return removeProhibitedValues(underlyingSource.generateRandomValues(randomNumberGenerator));
    }

    public void prohibitValue(Object value) {
        Optional<Boolean> underlyingSourceContainsValue = containsValue(value);
        if (!underlyingSourceContainsValue.isPresent() || underlyingSourceContainsValue.get())
            prohibitedValues.add(value);
    }

    private Optional<Boolean> containsValue(Object value) {
        if (underlyingSource instanceof CannedValuesFieldValueSource){
            Iterator<Object> allValues = underlyingSource.generateAllValues().iterator();
            boolean valueFound = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(allValues, Spliterator.ORDERED), false
            ).anyMatch(v -> v == value || (v != null && v.equals(value)));

            return Optional.of(valueFound);
        }

        return Optional.empty();
    }

    private Iterable<Object> removeProhibitedValues(Iterable<Object> values) {
        Iterator<Object> iterator = StreamSupport
            .stream(
                Spliterators.spliteratorUnknownSize(values.iterator(), Spliterator.ORDERED),
                false
            ).filter(value -> !prohibitedValues.contains(value))
            .iterator();

        return () -> iterator;
    }
}
