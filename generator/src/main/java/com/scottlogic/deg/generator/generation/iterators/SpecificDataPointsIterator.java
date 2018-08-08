package com.scottlogic.deg.generator.generation.iterators;

import java.util.LinkedList;
import java.util.Queue;

public class SpecificDataPointsIterator implements IFieldSpecIterator {
    private Queue<Object> values;

    public SpecificDataPointsIterator(Object... values) {
        this.values = new LinkedList<>();
        for (Object v : values)
            this.values.add(v);
    }

    @Override
    public boolean hasNext() {
        return !this.values.isEmpty();
    }

    @Override
    public Object next() {
        return this.values.remove();
    }

    @Override
    public boolean isInfinite() {
        return false;
    }
}
