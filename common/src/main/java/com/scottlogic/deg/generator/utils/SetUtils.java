package com.scottlogic.deg.generator.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetUtils {
    public static <T> Set<T> union(Set<T> a, Set<T> b) {
        return Stream
            .concat(a.stream(), b.stream())
            .collect(Collectors.toSet());
    }

    public static <T> Set<T> intersect(Set<T> a, Set<T> b) {
        final Set<T> intersection = new HashSet<>(a);
        intersection.retainAll(b);
        return intersection;
    }

    public static <T> Set<T> setOf(T e1, T e2, T e3) {
        return Stream.of(e1, e2, e3).collect(Collectors.toSet());
    }

    @SafeVarargs
    public static <T> Set<T> setOf(T ... elements) {
        return Stream.of(elements).collect(Collectors.toSet());
    }

    public static <T> Set<T> populateSet(Set<T> a, Set<T> b) {
        if (b != null) {
            a.addAll(b);
        }
        return a;
    }
}
