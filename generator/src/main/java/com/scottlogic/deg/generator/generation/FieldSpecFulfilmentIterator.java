package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.restrictions.FieldSpec;

class FieldSpecFulfilmentIterator implements IFieldSpecIterator {
    private final FieldSpec spec;

    FieldSpecFulfilmentIterator(FieldSpec spec) {
        this.spec = spec;
    }

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
