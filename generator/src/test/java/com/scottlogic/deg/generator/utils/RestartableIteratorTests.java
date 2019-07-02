Copyright 2019 Scott Logic Ltd /
/
Licensed under the Apache License, Version 2.0 (the \"License\");/
you may not use this file except in compliance with the License./
You may obtain a copy of the License at/
/
    http://www.apache.org/licenses/LICENSE-2.0/
/
Unless required by applicable law or agreed to in writing, software/
distributed under the License is distributed on an \"AS IS\" BASIS,/
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied./
See the License for the specific language governing permissions and/
limitations under the License.
package com.scottlogic.deg.generator.utils;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

class RestartableIteratorTests {
    @Test
    void next_underlyingIteratorHasValueAndFirstTimeCalling_returnsExpectedValue() {
        RestartableIterator<String> iterator = getIterator(new ArrayList<String>() {{ add("Test"); }});

        String value = iterator.next();

        Assert.assertEquals("Test", value);
    }

    @Test
    void next_underlyingIteratorHasMultipleValuesAndFirstTimeCalling_returnsAllExpectedValues() {
        RestartableIterator<String> iterator = getIterator(
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
        RestartableIterator<String> iterator = getIterator(
            new ArrayList<String>() {{
                add("First String");
                add("Second String");
                add("Third String");
            }}
        );

        iterator.next();
        iterator.next();
        iterator.next();
        iterator.restart();
        String firstValue = iterator.next();
        String secondValue = iterator.next();
        String thirdValue = iterator.next();

        Assert.assertEquals(firstValue, "First String");
        Assert.assertEquals(secondValue, "Second String");
        Assert.assertEquals(thirdValue, "Third String");
    }

    @Test
    void next_underlyingIteratorHasMultipleValuesAndResetCalledBeforeTheEndOfIterator_returnsAllExpectedValues() {
        RestartableIterator<String> iterator = getIterator(
            new ArrayList<String>() {{
                add("First String");
                add("Second String");
                add("Third String");
                add("Fourth String");
            }}
        );

        iterator.next();
        iterator.next();
        iterator.restart();
        String firstValue = iterator.next();
        String secondValue = iterator.next();
        String thirdValue = iterator.next();
        String fourthValue = iterator.next();

        Assert.assertEquals(firstValue, "First String");
        Assert.assertEquals(secondValue, "Second String");
        Assert.assertEquals(thirdValue, "Third String");
        Assert.assertEquals(fourthValue, "Fourth String");
    }

    @Test
    void next_underlyingIteratorHasNoMoreValues_throwsNoSuchElementException() {
        RestartableIterator<String> iterator = getIterator(
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
    void next_underlyingIteratorIsEmpty_throwsNoSuchElementException() {
        RestartableIterator<String> iterator = getIterator(new ArrayList<>());

        Assertions.assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void hasNext_underlyingIteratorHasNext_returnsTrue() {
        RestartableIterator iterator = getIterator(new ArrayList<String>() {{ add("Test"); }});

        boolean result = iterator.hasNext();

        Assert.assertTrue(result);
    }

    @Test
    void hasNext_underlyingIteratorHasEmittedAllValuesAndResetNotCalled_returnsFalse() {
        RestartableIterator iterator = getIterator(new ArrayList<String>() {{ add("Test"); }});

        iterator.next();
        boolean result = iterator.hasNext();

        Assert.assertFalse(result);
    }

    @Test
    void hasNext_underlyingIteratorHasEmittedAllValueAndResetCalled_returnsTrue() {
        RestartableIterator iterator = getIterator(new ArrayList<String>() {{ add("Test"); }});

        iterator.next();
        iterator.restart();
        boolean result = iterator.hasNext();

        Assert.assertTrue(result);
    }

    @Test
    void hasNext_underlyingIteratorHasEmittedAllValuesAndResetCalledAndCacheEmittedAllValues_returnsFalse() {
        RestartableIterator iterator = getIterator(new ArrayList<String>() {{ add("Test"); }});

        iterator.next();
        iterator.restart();
        iterator.next();
        boolean result = iterator.hasNext();

        Assert.assertFalse(result);
    }

    @Test
    void hasNext_underlyingIteratorEmpty_returnsFalse() {
        RestartableIterator iterator = getIterator(new ArrayList<>());

        boolean result = iterator.hasNext();

        Assert.assertFalse(result);
    }

    private RestartableIterator<String> getIterator(List<String> values) {
        return new RestartableIterator<>(values.iterator());
    }
}
