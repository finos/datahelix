package com.scottlogic.deg.generator.generation;

import java.util.Iterator;

class NullFulfilmentIterator implements IFieldSpecIterator {
    private boolean notCalled = true;

    @Override
    public boolean hasNext() {
        return notCalled;
    }

    @Override
    public Object next() {
        notCalled = false;
        return null;
    }

    @Override
    public boolean isInfinite() {
        return false;
    }
}
