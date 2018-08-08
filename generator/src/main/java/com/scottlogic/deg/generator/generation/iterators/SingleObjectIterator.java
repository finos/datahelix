package com.scottlogic.deg.generator.generation.iterators;

public class SingleObjectIterator implements IFieldSpecIterator {
    private boolean notCalled = true;
    private Object theObject;

    public SingleObjectIterator(Object theObject) {
        this.theObject = theObject;
    }

    @Override
    public boolean hasNext() {
        return notCalled;
    }

    @Override
    public Object next() {
        if (notCalled) {
            notCalled = false;
            return theObject;
        }
        return null;
    }

    @Override
    public boolean isInfinite() {
        return false;
    }
}
