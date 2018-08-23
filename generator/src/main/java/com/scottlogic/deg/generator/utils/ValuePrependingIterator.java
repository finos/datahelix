package com.scottlogic.deg.generator.utils;

import java.util.Iterator;

public class ValuePrependingIterator<T> implements Iterator<T>
{
    private final Iterator<T> underlyingIterator;
    private final T valueToInject;
    private boolean hasInjectedValue;

    public ValuePrependingIterator(Iterator<T> underlyingIterator, T valueToInject) {
        this.underlyingIterator = underlyingIterator;
        this.valueToInject = valueToInject;
    }

    @Override
    public boolean hasNext() {
        return !this.hasInjectedValue || this.underlyingIterator.hasNext();
    }

    @Override
    public T next() {
        if (!this.hasInjectedValue) {
            this.hasInjectedValue = true;
            return this.valueToInject;
        }

        return this.underlyingIterator.next();
    }
}
