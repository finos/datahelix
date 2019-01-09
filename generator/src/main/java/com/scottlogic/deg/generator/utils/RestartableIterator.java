package com.scottlogic.deg.generator.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RestartableIterator<T> implements Iterator<T> {
    private final List<T> cache = new ArrayList<>();
    private int index = 0;
    private final Iterator<T> underlyingIterator;

    public RestartableIterator(Iterator<T> underlyingIterator) {
        this.underlyingIterator = underlyingIterator;
    }

    @Override
    public boolean hasNext() {
        return index < cache.size() || underlyingIterator.hasNext();
    }

    @Override
    public T next() {
        T item;
        if (index < cache.size()) {
            item = cache.get(index);
        } else {
            item = underlyingIterator.next();
            cache.add(item);
        }

        index++;

        return item;
    }

    public void restart() {
        index = 0;
    }
}
