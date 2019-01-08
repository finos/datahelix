package com.scottlogic.deg.generator.utils;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

class RepeatableIteratorTests {
    @Test
    void next_underlyingIteratorHasValueAndFirstTimeCalling_returnsExpectedValue() {
        RepeatableIterator<String> iterator = getIterator(new ArrayList<String>() {{ add("Test"); }});

        String value = iterator.next();

        Assert.assertEquals("Test", value);
    }

    @Test
    void next_underlyingIteratorHasMultipleValuesAndFirstTimeCalling_returnsAllExpectedValues() {
        RepeatableIterator<String> iterator = getIterator(
            new ArrayList<String>() {{
                add("First String");
                add("Second String");
                add("Third String");
            }}
        );

        String firstValue = iterator.next();
        String secondValue = iterator.next();
        String thirdValue = iterator.next();

        Assert.assertEquals(firstValue, "First String");
        Assert.assertEquals(secondValue, "Second String");
        Assert.assertEquals(thirdValue, "Third String");
    }

    @Test
    void next_underlyingIteratorHasMultipleValuesAndResetCacheCalledAndNextCalled_returnsExpectedValuesMoreThanOnce() {
        RepeatableIterator<String> iterator = getIterator(
            new ArrayList<String>() {{
                add("First String");
                add("Second String");
                add("Third String");
            }}
        );

        iterator.next();
        iterator.next();
        iterator.next();
        iterator.resetCache();
        String firstValue = iterator.next();
        String secondValue = iterator.next();
        String thirdValue = iterator.next();

        Assert.assertEquals(firstValue, "First String");
        Assert.assertEquals(secondValue, "Second String");
        Assert.assertEquals(thirdValue, "Third String");
    }

    @Test
    void next_underlyingIteratorHasMultipleValuesAndResetCalledBeforeTheEndOfIterator_returnsAllExpectedValues() {
        RepeatableIterator<String> iterator = getIterator(
            new ArrayList<String>() {{
                add("First String");
                add("Second String");
                add("Third String");
                add("Fourth String");
            }}
        );

        iterator.next();
        iterator.next();
        iterator.resetCache();
        String firstValue = iterator.next();
        String secondValue = iterator.next();
        String thirdValue = iterator.next();
        String fourthValue = iterator.next();

        Assert.assertEquals(firstValue, "First String");
        Assert.assertEquals(secondValue, "Second String");
        Assert.assertEquals(thirdValue, "Third String");
        Assert.assertEquals(secondValue, "Second String");
        Assert.assertEquals(fourthValue, "Fourth String");
    }

    @Test
    void next_underlyingIteratorHasNoMoreValues_throwsException() {
        RepeatableIterator<String> iterator = getIterator(
            new ArrayList<String>() {{
                add("First String");
                add("Second String");
            }}
        );

        iterator.next();
        iterator.next();

        Assertions.assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void hasNext_underlyingIteratorHasNext_returnsTrue() {
        RepeatableIterator iterator = getIterator(new ArrayList<String>() {{ add("Test"); }});

        boolean result = iterator.hasNext();

        Assert.assertTrue(result);
    }

    @Test
    void hasNext_underlyingIteratorHasEmittedAllValuesAndResetNotCalled_returnsFalse() {
        RepeatableIterator iterator = getIterator(new ArrayList<String>() {{ add("Test"); }});

        iterator.next();
        boolean result = iterator.hasNext();

        Assert.assertFalse(result);
    }

    @Test
    void hasNext_underlyingIteratorHasEmittedAllValueAndResetCalled_returnsTrue() {
        RepeatableIterator iterator = getIterator(new ArrayList<String>() {{ add("Test"); }});

        iterator.next();
        iterator.resetCache();
        boolean result = iterator.hasNext();

        Assert.assertTrue(result);
    }

    @Test
    void hasNext_underlyingIteratorHasEmittedAllValuesAndResetCalledAndCacheEmittedAllValues_returnsFalse() {
        RepeatableIterator iterator = getIterator(new ArrayList<String>() {{ add("Test"); }});

        iterator.next();
        iterator.resetCache();
        iterator.next();
        boolean result = iterator.hasNext();

        Assert.assertFalse(result);
    }



    private RepeatableIterator<String> getIterator(List<String> values) {
        return new RepeatableIterator<>(values.iterator());
    }
}
