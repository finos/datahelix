package com.scottlogic.deg.generator.walker;

import com.google.common.collect.Iterators;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.Supplier;

/**
 * Emit values from the given Iterator<T> as many times as required
 * Provide a value for <maxItemsPerSource> to limit the number of items from the Iterator<T> before restarting it
 *
 * i.e. new SourceRepeatingIterator<T>(null, () -> Iterators.forArray(1, 2, 3)) -> 1, 2, 3
 * i.e. new SourceRepeatingIterator<T>(2, () -> Iterators.forArray(1, 2, 3)) -> 1, 2, 1, 2, ...
 *
 * @param <T> - The type of item to be emitted
 */
class SourceRepeatingIterator<T> implements Iterator<T> {
    private final Integer maxItemsPerSource;
    private final Supplier<Iterator<T>> getNextSource;
    private final boolean stopOnceSouceExhausted;
    private Integer itemsEmittedFromSource = 0;
    private Iterator<T> currentSource;

    SourceRepeatingIterator(
        Integer maxItemsPerSource,
        Supplier<Iterator<T>> getNextSource,
        boolean stopOnceSouceExhausted) {
        this.maxItemsPerSource = maxItemsPerSource;
        this.getNextSource = getNextSource;
        this.stopOnceSouceExhausted = stopOnceSouceExhausted;
    }

    @Override
    public boolean hasNext() {
        if (shouldResetSource()){
            currentSource = null;
        }

        if (currentSource == null){
            currentSource = getNextSource.get();
        }

        return currentSource != null && currentSource.hasNext();
    }

    private boolean shouldResetSource(){
        if (currentSource == null || currentSource.hasNext()){
            return false;
        }

        if (!stopOnceSouceExhausted){
            return true;
        }

        return false;
    }

    private void recordItemEmittedAndResetIfAtLimitForSource(){
        if (maxItemsPerSource == null){
            return;
        }

        itemsEmittedFromSource++;

        if (itemsEmittedFromSource >= maxItemsPerSource){
            //have emitted as many items from this source as permitted
            currentSource = null;
        }

        if (currentSource == null) {
            itemsEmittedFromSource = 0;
        }
    }

    @Override
    public T next() {
        T currentItem = currentSource.next();
        recordItemEmittedAndResetIfAtLimitForSource(); //will set currentSource to null if not permitted to yield any more items from currentSource

        return currentItem;
    }
}
