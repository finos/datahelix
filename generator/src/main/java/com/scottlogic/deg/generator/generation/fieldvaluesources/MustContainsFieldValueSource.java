package com.scottlogic.deg.generator.generation.fieldvaluesources;

import com.scottlogic.deg.generator.utils.ConcatenatingIterable;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MustContainsFieldValueSource implements FieldValueSource {
    private final Collection<ProhibitedFieldValueSource> mustContainRestrictionSources;

    public MustContainsFieldValueSource(List<ProhibitedFieldValueSource> mustContainRestrictionSources) {
        this.mustContainRestrictionSources = mustContainRestrictionSources;
    }

    public static MustContainsFieldValueSource of(FieldValueSource... mustContainRestrictionSources) {
        return of(Arrays.asList(mustContainRestrictionSources));
    }

    public static MustContainsFieldValueSource of(List<FieldValueSource> mustContainRestrictionSources) {
        return new MustContainsFieldValueSource(mustContainRestrictionSources
            .stream()
            .map(ProhibitedFieldValueSource::new)
            .collect(Collectors.toList()));
    }

    @Override
    public boolean isFinite() {
        return mustContainRestrictionSources.stream().allMatch(FieldValueSource::isFinite);
    }

    @Override
    public long getValueCount() {
        //might return a count greater than the number of values emitted, if duplicates are encountered
        return mustContainRestrictionSources.stream().mapToLong(FieldValueSource::getValueCount).sum();
    }

    @Override
    public Iterable<Object> generateInterestingValues() {
        //all values in this source are interesting
        return generateAllValues();
    }

    @Override
    public Iterable<Object> generateAllValues() {
        return new ConcatenatingIterable<>(
            mustContainRestrictionSources
                .stream()
                .map(FieldValueSource::generateAllValues)
                .collect(Collectors.toList())
        );
    }

    @Override
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
