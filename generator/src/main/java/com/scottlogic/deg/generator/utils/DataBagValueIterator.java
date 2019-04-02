package com.scottlogic.deg.generator.utils;

import com.scottlogic.deg.generator.DataBagValue;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecSource;

import java.util.Iterator;

public class DataBagValueIterator<T> implements Iterator<DataBagValue> {

    private Iterator<T> underlyingIterator;
    private String formatString;
    private final FieldSpecSource source;

    public DataBagValueIterator(Iterator<T> underlyingIterator, String formatString, FieldSpecSource source) {
        this.underlyingIterator = underlyingIterator;
        this.formatString = formatString;
        this.source = source;
    }

    @Override
    public boolean hasNext() {
        return this.underlyingIterator.hasNext();
    }

    @Override
    public DataBagValue next() {

        Object next = underlyingIterator.next();

        return new DataBagValue(next, formatString, source);
    }
}
