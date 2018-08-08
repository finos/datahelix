package com.scottlogic.deg.generator.generation.iterators;

import java.util.Iterator;

public class SetMembershipIterator implements IFieldSpecIterator {
    private Iterator<?> forwardedIterator;

    public SetMembershipIterator(Iterator<?> whitelistIterator) {
        this.forwardedIterator = whitelistIterator;
    }

    @Override
    public boolean isInfinite() {
        return false;
    }

    @Override
    public boolean hasNext() {
        return forwardedIterator.hasNext();
    }

    @Override
    public Object next() {
        return forwardedIterator.next();
    }
}
