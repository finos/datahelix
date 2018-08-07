package com.scottlogic.deg.generator;

import org.junit.Assert;

import java.util.Iterator;
import java.util.function.BiConsumer;

// classes can't be static in Java?
public class AssertUtils {
    private AssertUtils() {}

    public static <T, U> void pairwiseAssert(
        Iterable<T> a,
        Iterable<U> b,
        BiConsumer<T, U> asserter)
    {
        Iterator<T> aIterator = a.iterator();
        Iterator<U> bIterator = b.iterator();

        while (aIterator.hasNext() && bIterator.hasNext()) {
            asserter.accept(aIterator.next(), bIterator.next());
        }

        if (aIterator.hasNext() || bIterator.hasNext())
            Assert.fail("Sequences had different numbers of elements");
    }
}
