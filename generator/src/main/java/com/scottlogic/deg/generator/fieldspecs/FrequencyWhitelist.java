package com.scottlogic.deg.generator.fieldspecs;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class FrequencyWhitelist<T> implements Whitelist<T> {

    private static final FrequencyWhitelist<?> EMPTY = new FrequencyWhitelist<>(Collections.emptySet());

    private final Set<Entry<T>> underlyingSet;

    public FrequencyWhitelist(final Set<Entry<T>> underlyingSet) {
        this.underlyingSet = underlyingSet;
    }

    public static <T> FrequencyWhitelist<T> uniform(final Set<T> underlyingSet) {
        return new FrequencyWhitelist<>(
            underlyingSet.stream()
            .map(e -> new Entry<T>(e, 1.0F))
            .collect(Collectors.toSet()));
    }

    public static <T> FrequencyWhitelist<T> empty() {
        return (FrequencyWhitelist<T>)EMPTY;
    }

    public Set<T> set() {
        return underlyingSet.stream().map(Entry::element).collect(Collectors.toSet());
    }

    public Set<Entry<T>> distributedSet() {
        return underlyingSet;
    }

    public static class Entry<E> {

        private final E element;

        private final float frequency;

        public Entry(final E element, final float frequency) {
            this.element = element;
            this.frequency = frequency;
        }

        public E element() {
            return element;
        }

        public float frequency() {
            return frequency;
        }
    }
}
