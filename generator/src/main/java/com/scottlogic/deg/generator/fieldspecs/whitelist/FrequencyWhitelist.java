package com.scottlogic.deg.generator.fieldspecs.whitelist;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FrequencyWhitelist<T> implements Whitelist<T> {

    private static final FrequencyWhitelist<?> EMPTY = new FrequencyWhitelist<>(Collections.emptySet());

    private final Set<ElementFrequency<T>> underlyingSet;

    public FrequencyWhitelist(final Set<ElementFrequency<T>> underlyingSet) {
        float total = underlyingSet.stream()
            .map(ElementFrequency::frequency)
            .reduce(0.0F, Float::sum);

        this.underlyingSet = underlyingSet.stream()
            .map(holder -> new ElementFrequency<>(holder.element(), holder.frequency() / total))
            .collect(Collectors.toSet());
    }

    public static <T> FrequencyWhitelist<T> uniform(final Set<T> underlyingSet) {
        return new FrequencyWhitelist<>(
            underlyingSet.stream()
            .map(e -> new ElementFrequency<T>(e, 1.0F))
            .collect(Collectors.toSet()));
    }

    @SuppressWarnings("unchecked")
    public static <T> FrequencyWhitelist<T> empty() {
        return (FrequencyWhitelist<T>)EMPTY;
    }

    @Override
    public Set<T> set() {
        return underlyingSet.stream().map(ElementFrequency::element).collect(Collectors.toSet());
    }

    @Override
    public Set<ElementFrequency<T>> distributedSet() {
        return underlyingSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FrequencyWhitelist<?> that = (FrequencyWhitelist<?>) o;
        return Objects.equals(underlyingSet, that.underlyingSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(underlyingSet);
    }
}
