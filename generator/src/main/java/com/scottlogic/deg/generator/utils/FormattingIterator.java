package com.scottlogic.deg.generator.utils;

import com.scottlogic.deg.generator.DataBagValue;

import java.math.BigDecimal;
import java.util.Iterator;

public class FormattingIterator<T> implements Iterator<DataBagValue> {

    private Iterator<T> underlyingIterator;
    private String formatString;

    public FormattingIterator(Iterator<T> underlyingIterator, String formatString) {
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
