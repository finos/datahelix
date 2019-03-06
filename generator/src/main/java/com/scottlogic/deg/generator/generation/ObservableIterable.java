package com.scottlogic.deg.generator.generation;

import java.util.Iterator;
import java.util.Observable;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

/**
 * An iterable that will notify any observers as each item is emitted
 *
 * @param <T> The item being iterated over
 */
public class ObservableIterable<T> extends Observable implements Iterable<T> {
    private final Iterable<T> values;

    public ObservableIterable(Iterable<T> values) {
        this.values = values;
    }

    @Override
    public Iterator<T> iterator() {
        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(values.iterator(), Spliterator.ORDERED), false
        ).peek(v -> {
            super.setChanged();
            super.notifyObservers(v);
        }).iterator();
    }
}
