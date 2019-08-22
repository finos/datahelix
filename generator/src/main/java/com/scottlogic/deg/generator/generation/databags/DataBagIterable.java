package com.scottlogic.deg.generator.generation.databags;

import java.util.Iterator;

public class DataBagIterable {
    private final boolean unique;
    private final Iterator<DataBag> iterator;

    public DataBagIterable(Iterator<DataBag> iterator, boolean unique) {
        this.unique = unique;
        this.iterator = iterator;
    }

    public boolean isUnique() {
        return unique;
    }

    public Iterator<DataBag> iterator() {
        return iterator;
    }
}
