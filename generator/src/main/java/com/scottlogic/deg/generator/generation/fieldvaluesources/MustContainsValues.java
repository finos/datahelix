package com.scottlogic.deg.generator.generation.fieldvaluesources;

import com.scottlogic.deg.generator.utils.ConcatenatingIterable;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A type capable of emitting all values from each must-contain field-value-source
 *
 * Will represent each must-contain FieldValueSource as a ProhibitedFieldValueSource which can prohibit
 * values from being emitted, i.e. if they are emitted by another FieldValueSource.
 */
public class MustContainsValues {
    private final Collection<ProhibitedFieldValueSource> mustContainRestrictionSources;

    MustContainsValues(List<ProhibitedFieldValueSource> mustContainRestrictionSources) {
        this.mustContainRestrictionSources = mustContainRestrictionSources;
    }

    public static MustContainsValues of(FieldValueSource... mustContainRestrictionSources) {
        return of(Arrays.asList(mustContainRestrictionSources));
    }

    public static MustContainsValues of(List<FieldValueSource> mustContainRestrictionSources) {
        return new MustContainsValues(mustContainRestrictionSources
            .stream()
            .map(ProhibitedFieldValueSource::new)
            .collect(Collectors.toList()));
    }

    public boolean isFinite() {
        return mustContainRestrictionSources.stream().allMatch(FieldValueSource::isFinite);
    }

    public long getValueCount() {
        //might return a count greater than the number of values emitted, if duplicates are encountered
        return mustContainRestrictionSources.stream().mapToLong(FieldValueSource::getValueCount).sum();
    }

    public Iterable<Object> generateAllValues() {
        return new ConcatenatingIterable<>(
            mustContainRestrictionSources
                .stream()
                .map(FieldValueSource::generateAllValues)
                .collect(Collectors.toList())
        );
    }

    public Iterable<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        //this method will only be called if all preceding value sources emit no values
        //therefore this is the last-chance-saloon for some random values
        //it should emit an infinite 'stream' of randomly selected values from this type

        List<Object> allValues = new ArrayList<>();
        generateAllValues().forEach(allValues::add);

        if (allValues.isEmpty()){
            return Collections::emptyIterator;
        }

        Stream<Object> infiniteRandomlySelectingStream = Stream.generate(() -> {
            int indexOfNextValue = randomNumberGenerator.nextInt(allValues.size());
            return allValues.get(indexOfNextValue);
        });

        Iterator<Object> iterator = infiniteRandomlySelectingStream.iterator();
        return () -> iterator;
    }

    void removeValue(Object value) {
        mustContainRestrictionSources.forEach(vs -> removeValueFromMustContains(vs, value));
    }

    private void removeValueFromMustContains(ProhibitedFieldValueSource valueSource, Object value) {
        valueSource.prohibitValue(value);
    }
}
