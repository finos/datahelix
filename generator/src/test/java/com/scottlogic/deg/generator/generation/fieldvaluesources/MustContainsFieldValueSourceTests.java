package com.scottlogic.deg.generator.generation.fieldvaluesources;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

class MustContainsFieldValueSourceTests {
    @Test
    public void isFinite_whenAllFinite_shouldReturnTrue(){
        ProhibitedFieldValueSource mustContain1 = mock(ProhibitedFieldValueSource.class);
        ProhibitedFieldValueSource mustContain2 = mock(ProhibitedFieldValueSource.class);
        MustContainsFieldValueSource mustContainSource = new MustContainsFieldValueSource(
            Arrays.asList(mustContain1, mustContain2));
        when(mustContain1.isFinite()).thenReturn(true);
        when(mustContain2.isFinite()).thenReturn(true);

        boolean isFinite = mustContainSource.isFinite();

        Assert.assertThat(isFinite, is(true));
    }

    @Test
    public void isFinite_whenSomeFinite_shouldReturnFalse(){
        ProhibitedFieldValueSource mustContain1 = mock(ProhibitedFieldValueSource.class);
        ProhibitedFieldValueSource mustContain2 = mock(ProhibitedFieldValueSource.class);
        MustContainsFieldValueSource mustContainSource = new MustContainsFieldValueSource(
            Arrays.asList(mustContain1, mustContain2));
        when(mustContain1.isFinite()).thenReturn(true);
        when(mustContain2.isFinite()).thenReturn(false);

        boolean isFinite = mustContainSource.isFinite();

        Assert.assertThat(isFinite, is(false));
    }

    @Test
    public void isFinite_whenNoneFinite_shouldReturnFalse(){
        ProhibitedFieldValueSource mustContain1 = mock(ProhibitedFieldValueSource.class);
        ProhibitedFieldValueSource mustContain2 = mock(ProhibitedFieldValueSource.class);
        MustContainsFieldValueSource mustContainSource = new MustContainsFieldValueSource(
            Arrays.asList(mustContain1, mustContain2));
        when(mustContain1.isFinite()).thenReturn(false);
        when(mustContain2.isFinite()).thenReturn(false);

        boolean isFinite = mustContainSource.isFinite();

        Assert.assertThat(isFinite, is(false));
    }

    @Test
    public void isFinite_whenEmpty_shouldReturnTrue(){
        MustContainsFieldValueSource mustContainSource = new MustContainsFieldValueSource(
            Collections.emptyList());

        boolean isFinite = mustContainSource.isFinite();

        Assert.assertThat(isFinite, is(true));
    }

    @Test
    public void getValueCount_whenEmpty_shouldReturn0(){
        MustContainsFieldValueSource mustContainSource = new MustContainsFieldValueSource(
            Collections.emptyList());

        long count = mustContainSource.getValueCount();

        Assert.assertThat(count, is(0L));
    }

    @Test
    public void getValueCount_whenPresent_shouldReturnSum(){
        ProhibitedFieldValueSource mustContain1 = mock(ProhibitedFieldValueSource.class);
        ProhibitedFieldValueSource mustContain2 = mock(ProhibitedFieldValueSource.class);
        MustContainsFieldValueSource mustContainSource = new MustContainsFieldValueSource(
            Arrays.asList(mustContain1, mustContain2));
        when(mustContain1.getValueCount()).thenReturn(1L);
        when(mustContain2.getValueCount()).thenReturn(2L);

        long count = mustContainSource.getValueCount();

        Assert.assertThat(count, is(3L));
    }

    @Test
    public void generateInterestingValues_shouldReturnValuesFromAllSources(){
        ProhibitedFieldValueSource mustContain1 = mock(ProhibitedFieldValueSource.class);
        ProhibitedFieldValueSource mustContain2 = mock(ProhibitedFieldValueSource.class);
        MustContainsFieldValueSource mustContainSource = new MustContainsFieldValueSource(
            Arrays.asList(mustContain1, mustContain2));
        when(mustContain1.generateAllValues()).thenReturn(Collections.singletonList("foo"));
        when(mustContain2.generateAllValues()).thenReturn(Collections.singletonList("bar"));

        Iterable<Object> result = mustContainSource.generateInterestingValues();

        Assert.assertThat(result, hasItems("foo", "bar"));
    }

    @Test
    public void generateAllValues_shouldReturnValuesFromAllSources(){
        ProhibitedFieldValueSource mustContain1 = mock(ProhibitedFieldValueSource.class);
        ProhibitedFieldValueSource mustContain2 = mock(ProhibitedFieldValueSource.class);
        MustContainsFieldValueSource mustContainSource = new MustContainsFieldValueSource(
            Arrays.asList(mustContain1, mustContain2));
        when(mustContain1.generateAllValues()).thenReturn(Collections.singletonList("foo"));
        when(mustContain2.generateAllValues()).thenReturn(Collections.singletonList("bar"));

        Iterable<Object> result = mustContainSource.generateAllValues();

        Assert.assertThat(result, hasItems("foo", "bar"));
    }

    @Test
    public void generateRandomValues_shouldReturnValuesFromAllSources(){
        ProhibitedFieldValueSource mustContain1 = mock(ProhibitedFieldValueSource.class);
        ProhibitedFieldValueSource mustContain2 = mock(ProhibitedFieldValueSource.class);
        MustContainsFieldValueSource mustContainSource = new MustContainsFieldValueSource(
            Arrays.asList(mustContain1, mustContain2));
        RandomNumberGenerator randomNumberGenerator = mock(RandomNumberGenerator.class);
        AtomicInteger nextRandomIndex = new AtomicInteger();
        when(mustContain1.generateAllValues()).thenReturn(Collections.singletonList("foo"));
        when(mustContain2.generateAllValues()).thenReturn(Collections.singletonList("bar"));
        when(randomNumberGenerator.nextInt(2)).then(__ -> nextRandomIndex.getAndIncrement());

        Iterable<Object> result = mustContainSource.generateRandomValues(randomNumberGenerator);

        Stream<Object> stream = StreamSupport.stream(Spliterators.spliteratorUnknownSize(result.iterator(), Spliterator.ORDERED), false);
        Assert.assertThat(stream.limit(2).collect(Collectors.toList()), hasItems("foo", "bar"));
    }

    @Test
    public void removeValue_whenEmpty_shouldSucceed(){
        MustContainsFieldValueSource mustContainSource = new MustContainsFieldValueSource(
            Collections.emptyList());
        Object value = "foo";

        Assertions.assertDoesNotThrow(() -> mustContainSource.removeValue(value));
    }

    @Test
    public void removeValue_whenContainsOneSource_shouldRemoveMustContainValue(){
        ProhibitedFieldValueSource mustContain1 = mock(ProhibitedFieldValueSource.class);
        MustContainsFieldValueSource mustContainSource = new MustContainsFieldValueSource(
            Collections.singletonList(mustContain1));
        Object value = "foo";

        mustContainSource.removeValue(value);

        verify(mustContain1).prohibitValue("foo");
    }

    @Test
    public void removeValue_whenContainsMultipleSources_shouldRemoveMustContainValueFromAllSources(){
        ProhibitedFieldValueSource mustContain1 = mock(ProhibitedFieldValueSource.class);
        ProhibitedFieldValueSource mustContain2 = mock(ProhibitedFieldValueSource.class);
        MustContainsFieldValueSource mustContainSource = new MustContainsFieldValueSource(
            Arrays.asList(mustContain1, mustContain2));
        Object value = "foo";

        mustContainSource.removeValue(value);

        verify(mustContain1).prohibitValue("foo");
        verify(mustContain2).prohibitValue("foo");
    }
}