package com.scottlogic.deg.generator.utils;

import java.util.Iterator;
import java.util.stream.Stream;

public class StreamAsIterable<T> implements Iterable<T> {
    private final Stream<T> stream;

    public StreamAsIterable(Stream<T> stream) {
        this.stream = stream;
    }


    @Override
    public Iterator<T> iterator() {
        return stream.iterator();
    }
}
