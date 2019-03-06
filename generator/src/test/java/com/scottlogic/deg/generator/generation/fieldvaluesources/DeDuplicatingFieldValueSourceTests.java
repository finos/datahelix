package com.scottlogic.deg.generator.generation.fieldvaluesources;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

class DeDuplicatingFieldValueSourceTests {
    @Test
    public void isFinite_whenOriginalSourceIsFinite_shouldReturnTrue(){
        FieldValueSource originalSource = mock(FieldValueSource.class);
        MustContainsValues mustContainsSource = mock(MustContainsValues.class);
        DeDuplicatingFieldValueSource deduplicatingSource = new DeDuplicatingFieldValueSource(
            originalSource,
            mustContainsSource);
        when(originalSource.isFinite()).thenReturn(true);

        boolean isFinite = deduplicatingSource.isFinite();

        Assert.assertThat(isFinite, is(true));
    }

    @Test
    public void isFinite_whenOriginalSourceIsNotFinite_shouldReturnFalse(){
        FieldValueSource originalSource = mock(FieldValueSource.class);
        MustContainsValues mustContainsSource = mock(MustContainsValues.class);
        DeDuplicatingFieldValueSource deduplicatingSource = new DeDuplicatingFieldValueSource(
            originalSource,
            mustContainsSource);
        when(originalSource.isFinite()).thenReturn(false);

        boolean isFinite = deduplicatingSource.isFinite();

        Assert.assertThat(isFinite, is(false));
    }

    @Test
    public void getValueCount_shouldReturnSumValueCounts(){
        FieldValueSource originalSource = mock(FieldValueSource.class);
        MustContainsValues mustContainsSource = mock(MustContainsValues.class);
        DeDuplicatingFieldValueSource deduplicatingSource = new DeDuplicatingFieldValueSource(
            originalSource,
            mustContainsSource);
        when(originalSource.getValueCount()).thenReturn(1L);
        when(mustContainsSource.getValueCount()).thenReturn(2L);

        long valueCount = deduplicatingSource.getValueCount();

        Assert.assertThat(valueCount, is(3L));
    }

    @Test
    public void generateInterestingValues_shouldRequestInterestingValues(){
        FieldValueSource originalSource = mock(FieldValueSource.class);
        MustContainsValues mustContainsSource = mock(MustContainsValues.class);
        DeDuplicatingFieldValueSource deduplicatingSource = new DeDuplicatingFieldValueSource(
            originalSource,
            mustContainsSource);

        deduplicatingSource.generateInterestingValues();

        verify(originalSource).generateInterestingValues();
    }

    @Test
    public void generateInterestingValues_shouldRequestAllMustContainValues(){
        FieldValueSource originalSource = mock(FieldValueSource.class);
        MustContainsValues mustContainsSource = mock(MustContainsValues.class);
        DeDuplicatingFieldValueSource deduplicatingSource = new DeDuplicatingFieldValueSource(
            originalSource,
            mustContainsSource);
        when(originalSource.generateInterestingValues()).thenReturn(Collections.emptyList());
        when(mustContainsSource.generateAllValues()).thenReturn(Collections.emptyList());

        deduplicatingSource.generateInterestingValues()
            .forEach(v -> { /* to exhaust the interesting values */ });

        verify(mustContainsSource).generateAllValues();
    }

    @Test
    public void generateInterestingValues_whenOriginalSourceEmitsAValue_shouldEnsureValueIsNotRepeated(){
        FieldValueSource originalSource = CannedValuesFieldValueSource.of("foo", "bar");
        MustContainsValues mustContainsSource = MustContainsValues.of(
            CannedValuesFieldValueSource.of("foo"));
        DeDuplicatingFieldValueSource deduplicatingSource = new DeDuplicatingFieldValueSource(
            originalSource,
            mustContainsSource);

        Iterable<Object> result = deduplicatingSource.generateInterestingValues();

        Assert.assertThat(result, hasItems("foo", "bar"));
    }

    @Test
    public void generateInterestingValues_whenOriginalSourceDoesNotEmitARequiredValue_shouldEnsureValueIsEmitted(){
        FieldValueSource originalSource = CannedValuesFieldValueSource.of("foo");
        MustContainsValues mustContainsSource = MustContainsValues.of(
            CannedValuesFieldValueSource.of("bar"));
        DeDuplicatingFieldValueSource deduplicatingSource = new DeDuplicatingFieldValueSource(
            originalSource,
            mustContainsSource);

        Iterable<Object> result = deduplicatingSource.generateInterestingValues();

        Assert.assertThat(result, hasItems("foo", "bar"));
    }

    @Test
    public void generateAllValues_shouldRequestAllValues(){
        FieldValueSource originalSource = mock(FieldValueSource.class);
        MustContainsValues mustContainsSource = mock(MustContainsValues.class);
        DeDuplicatingFieldValueSource deduplicatingSource = new DeDuplicatingFieldValueSource(
            originalSource,
            mustContainsSource);

        deduplicatingSource.generateAllValues();

        verify(originalSource).generateAllValues();
    }

    @Test
    public void generateAllValues_shouldRequestAllMustContainValues(){
        FieldValueSource originalSource = mock(FieldValueSource.class);
        MustContainsValues mustContainsSource = mock(MustContainsValues.class);
        DeDuplicatingFieldValueSource deduplicatingSource = new DeDuplicatingFieldValueSource(
            originalSource,
            mustContainsSource);
        when(originalSource.generateAllValues()).thenReturn(Collections.emptyList());
        when(mustContainsSource.generateAllValues()).thenReturn(Collections.emptyList());

        deduplicatingSource.generateAllValues()
            .forEach(v -> { /* to exhaust all values */});

        verify(mustContainsSource).generateAllValues();
    }

    @Test
    public void generateAllValues_whenOriginalSourceEmitsAValue_shouldEnsureValueIsNotRepeated(){
        FieldValueSource originalSource = CannedValuesFieldValueSource.of("foo", "bar");
        MustContainsValues mustContainsSource = MustContainsValues.of(
            CannedValuesFieldValueSource.of("foo"));
        DeDuplicatingFieldValueSource deduplicatingSource = new DeDuplicatingFieldValueSource(
            originalSource,
            mustContainsSource);

        Iterable<Object> result = deduplicatingSource.generateAllValues();

        Assert.assertThat(result, hasItems("foo", "bar"));
    }

    @Test
    public void generateAllValues_whenOriginalSourceDoesNotEmitARequiredValue_shouldEnsureValueIsEmitted(){
        FieldValueSource originalSource = CannedValuesFieldValueSource.of("foo");
        MustContainsValues mustContainsSource = MustContainsValues.of(
            CannedValuesFieldValueSource.of("bar"));
        DeDuplicatingFieldValueSource deduplicatingSource = new DeDuplicatingFieldValueSource(
            originalSource,
            mustContainsSource);

        Iterable<Object> result = deduplicatingSource.generateAllValues();

        Assert.assertThat(result, hasItems("foo", "bar"));
    }

    @Test
    public void generateRandomValues_shouldRequestRandomValues(){
        FieldValueSource originalSource = mock(FieldValueSource.class);
        MustContainsValues mustContainsSource = mock(MustContainsValues.class);
        RandomNumberGenerator randomNumberGenerator = mock(RandomNumberGenerator.class);
        DeDuplicatingFieldValueSource deduplicatingSource = new DeDuplicatingFieldValueSource(
            originalSource,
            mustContainsSource);

        deduplicatingSource.generateRandomValues(randomNumberGenerator);

        verify(originalSource).generateRandomValues(randomNumberGenerator);
    }

    @Test
    public void generateRandomValues_shouldRequestAllMustContainValues(){
        FieldValueSource originalSource = mock(FieldValueSource.class);
        MustContainsValues mustContainsSource = mock(MustContainsValues.class);
        RandomNumberGenerator randomNumberGenerator = mock(RandomNumberGenerator.class);
        DeDuplicatingFieldValueSource deduplicatingSource = new DeDuplicatingFieldValueSource(
            originalSource,
            mustContainsSource);
        when(originalSource.generateRandomValues(any())).thenReturn(Collections.emptyList());
        when(mustContainsSource.generateRandomValues(any())).thenReturn(Collections.emptyList());

        deduplicatingSource.generateRandomValues(randomNumberGenerator)
            .forEach(v -> { /* to exhaust the random values */ });

        verify(mustContainsSource).generateRandomValues(randomNumberGenerator);
    }

    @Test
    public void generateRandomValues_whenOriginalSourceEmitsAValue_shouldEnsureValueIsNotRepeated(){
        FieldValueSource originalSource = CannedValuesFieldValueSource.of("foo", "bar");
        MustContainsValues mustContainsSource = MustContainsValues.of(
            CannedValuesFieldValueSource.of("foo"));
        RandomNumberGenerator randomNumberGenerator = mock(RandomNumberGenerator.class);
        DeDuplicatingFieldValueSource deduplicatingSource = new DeDuplicatingFieldValueSource(
            originalSource,
            mustContainsSource);
        AtomicInteger nextRandomIndex = new AtomicInteger();
        when(randomNumberGenerator.nextInt(2)).then(__ -> nextRandomIndex.getAndIncrement());

        Iterable<Object> result = deduplicatingSource.generateRandomValues(randomNumberGenerator);

        Stream<Object> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(result.iterator(), Spliterator.ORDERED), false);
        Assert.assertThat(stream.limit(2).collect(Collectors.toList()), hasItems("foo", "bar"));
    }

    @Test
    public void generateRandomValues_whenOriginalSourceDoesNotEmitARequiredValue_shouldEnsureValueIsEmitted(){
        FieldValueSource originalSource = CannedValuesFieldValueSource.of("foo");
        MustContainsValues mustContainsSource = MustContainsValues.of(
            CannedValuesFieldValueSource.of("bar"));
        RandomNumberGenerator randomNumberGenerator = mock(RandomNumberGenerator.class);
        DeDuplicatingFieldValueSource deduplicatingSource = new DeDuplicatingFieldValueSource(
            originalSource,
            mustContainsSource);

        Iterable<Object> result = deduplicatingSource.generateRandomValues(randomNumberGenerator);

        Stream<Object> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(result.iterator(), Spliterator.ORDERED), false);
        //randomly selected values from originalSource, which will simply repeat the same value <foo> infinitely
        Assert.assertThat(stream.limit(2).collect(Collectors.toList()), hasItems("foo", "foo"));
    }
}