package com.scottlogic.deg.generator.fieldspecs;

import java.util.Collections;
import java.util.Set;

public class FrequencyWhitelist<T> implements Whitelist<T> {

    private static final FrequencyWhitelist<?> EMPTY = new FrequencyWhitelist<>(Collections.emptySet());

    private final Set<T> underlyingSet;

    public FrequencyWhitelist(final Set<T> underlyingSet) {
        this.underlyingSet = underlyingSet;
    }

    public static <T> FrequencyWhitelist<T> empty() {
        return (FrequencyWhitelist<T>)EMPTY;
    }

    public Set<T> set() {
        return underlyingSet;
    }
}
