package com.scottlogic.deg.generator.generation.iterators;

import javax.naming.OperationNotSupportedException;

public class UnfulfillableIterator implements IFieldSpecIterator {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Object next() {
        throw new IllegalStateException();
    }

    @Override
    public boolean isInfinite() {
        return false;
    }
}
