package com.scottlogic.deg.generator.utils;

import java.util.function.Function;
import java.util.function.Predicate;

public class IterableUtils {
    public static Iterable<String> wrapIterableWithProjectionAndFilter(
        Iterable<String> iterable,
        Function<String, String> projection,
        Predicate<String> predicate
    ) {
        return new FilteringIterable<>(new ProjectingIterable<>(iterable, projection), predicate);
    }

    public static Iterable<String> wrapIterableWithNonEmptyStringCheck(Iterable<String> iterable) {
        return new FilteringIterable<>(iterable, s -> !s.isEmpty());
    }
}
