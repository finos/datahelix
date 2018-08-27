package com.scottlogic.deg.generator.utils;

import java.util.Iterator;

public class UpCastingIterator<TFrom extends TTo, TTo> implements Iterator<TTo> {
    private final Iterator<TFrom> underlyingIterator;

    public UpCastingIterator(Iterator<TFrom> underlyingIterator) {
        this.underlyingIterator = underlyingIterator;
    }

    @Override
    public boolean hasNext() {
        return underlyingIterator.hasNext();
    }

    @Override
    public TTo next() {
        return underlyingIterator.next();
    }
}
