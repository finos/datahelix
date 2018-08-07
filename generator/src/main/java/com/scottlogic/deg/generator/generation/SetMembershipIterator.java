package com.scottlogic.deg.generator.generation;

import java.util.Iterator;

class SetMembershipIterator implements IFieldSpecIterator {
    private Iterator<?> forwardedIterator;

    SetMembershipIterator(Iterator<?> whitelistIterator) {
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
