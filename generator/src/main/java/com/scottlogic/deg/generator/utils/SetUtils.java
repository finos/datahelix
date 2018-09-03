package com.scottlogic.deg.generator.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class SetUtils {
    public static <T> Set<T> union(Set<T> a, Set<T> b) {
        return Stream
            .concat(a.stream(), b.stream())
            .collect(
                HashSet::new,
                HashSet::add,
                HashSet::addAll);
    }

    public static <T> Set<T> intersect(Set<T> a, Set<T> b) {
        final Set<T> intersection = new HashSet<>(a);
        intersection.retainAll(b);
        return intersection;
    }
}
