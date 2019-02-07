package com.scottlogic.deg.generator.walker;

import java.util.Iterator;
import java.util.function.Supplier;

class SourceRepeatingIterator<T> implements Iterator<T> {
    private final Integer maxItemsPerSource;
    private final Supplier<Iterator<T>> getNextSource;
    private Integer itemsEmittedFromSource = 0;
    private Iterator<T> currentSource;

    SourceRepeatingIterator(
        Integer maxItemsPerSource,
        Supplier<Iterator<T>> getNextSource) {

        this.maxItemsPerSource = maxItemsPerSource;
        this.getNextSource = getNextSource;
    }

    @Override
    public boolean hasNext() {
        if (currentSource != null && !currentSource.hasNext()){
            currentSource = null;
        }

        if (currentSource == null){
            currentSource = getNextSource.get();
        }

        return currentSource != null && currentSource.hasNext();
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
