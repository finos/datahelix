package com.scottlogic.deg.generator.generation;

public class UnfulfillableIterator implements IFieldSpecIterator {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Object next() {
        return null;
    }

    @Override
    public boolean isInfinite() {
        return false;
    }
}
