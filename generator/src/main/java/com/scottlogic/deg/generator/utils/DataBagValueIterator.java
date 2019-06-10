package com.scottlogic.deg.generator.utils;

import com.scottlogic.deg.generator.generation.databags.DataBagValue;
import com.scottlogic.deg.common.output.DataBagValueSource;

import java.util.Iterator;

public class DataBagValueIterator<T> implements Iterator<DataBagValue> {

    private Iterator<T> underlyingIterator;
    private String formatString;
    private final DataBagValueSource source;

    public DataBagValueIterator(Iterator<T> underlyingIterator, String formatString, DataBagValueSource source) {
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
