package com.scottlogic.deg.generator.utils;

import com.scottlogic.deg.generator.generation.databags.DataBagValue;

import java.util.Iterator;

public class DataBagValueIterator<T> implements Iterator<DataBagValue> {

    private Iterator<T> underlyingIterator;
    private String formatString;

    public DataBagValueIterator(Iterator<T> underlyingIterator, String formatString) {
        this.underlyingIterator = underlyingIterator;
        this.formatString = formatString;
    }

    @Override
    public boolean hasNext() {
        return this.underlyingIterator.hasNext();
    }

    @Override
    public DataBagValue next() {

        Object next = underlyingIterator.next();

        return new DataBagValue(next, formatString);
    }
}
