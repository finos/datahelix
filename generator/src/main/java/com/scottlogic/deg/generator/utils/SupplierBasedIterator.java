package com.scottlogic.deg.generator.utils;

import java.util.Iterator;
import java.util.function.Supplier;

public class SupplierBasedIterator<T> implements Iterator<T> {
    private final Supplier<T> supplier;

    public SupplierBasedIterator(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public T next() {
        return supplier.get();
    }
}

