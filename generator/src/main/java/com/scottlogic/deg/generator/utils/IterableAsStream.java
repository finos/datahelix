package com.scottlogic.deg.generator.utils;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class IterableAsStream {
    public static <T> Stream<T> convert(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    // not a real instance class
    private IterableAsStream() {}
}
