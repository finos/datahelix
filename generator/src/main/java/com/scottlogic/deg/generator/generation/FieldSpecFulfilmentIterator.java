package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.generation.tmpReducerOutput.FieldSpec;

import java.util.Iterator;

class FieldSpecFulfilmentIterator implements Iterator<Object> {
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
}
