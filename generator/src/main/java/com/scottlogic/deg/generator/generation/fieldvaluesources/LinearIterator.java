package com.scottlogic.deg.generator.generation.fieldvaluesources;

import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;

import java.util.Iterator;

public class LinearIterator<T extends Comparable<T>> implements Iterator<T> {
    private final LinearRestrictions<T> linearRestrictions;
    private T next;

    public LinearIterator(LinearRestrictions<T> linearRestrictions) {
        this.linearRestrictions = linearRestrictions;
        next = linearRestrictions.getMin().getValue();
        if(!linearRestrictions.getMin().isInclusive()){
            next();
        }
    }

    @Override
    public boolean hasNext() {
        return linearRestrictions.getMax().isAfter(next);
    }

    @Override
    public T next() {
        T copy = next;
        next = linearRestrictions.getGranularity().getNext(next);
        return copy;
    }
}
