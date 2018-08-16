package com.scottlogic.deg.generator.utils;

import java.math.BigDecimal;
import java.util.Iterator;

public class FormattingIterator<T> implements Iterator<Object> {

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
    public Object next() {

        Object next = underlyingIterator.next();

         String formattedValue = next != null ?
                 String.format(formatString, next) : null;

         // If we just formatted a number, lets see if we can convert it back
        if(next instanceof BigDecimal){
            try {
                return new BigDecimal(formattedValue);
            } catch (NumberFormatException ex) {

            }
        }

        return formattedValue;
    }
}
