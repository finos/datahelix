package com.scottlogic.deg.generator.utils;

import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

public class ProjectingIterable<TFrom, TTo> implements Iterable<TTo>
{
    private final Iterable<TFrom> underlyingIterable;
    private final Function<TFrom, TTo> converter;

    public ProjectingIterable(
        Iterable<TFrom> underlyingIterable,
        Function<TFrom, TTo> converter) {

        this.underlyingIterable = underlyingIterable;
        this.converter = converter;
    }


    @Override
    public Iterator<TTo> iterator() {
        return new InternalIterator(
            this.underlyingIterable.iterator(),
            this.converter);
    }

    class InternalIterator implements Iterator<TTo>
    {
        private final Iterator<TFrom> sourceIterator;
        private final Function<TFrom, TTo> converter;

        InternalIterator(
            Iterator<TFrom> sourceIterator,
            Function<TFrom, TTo> converter) {

            this.sourceIterator = sourceIterator;
            this.converter = converter;
        }

        @Override
        public boolean hasNext() {
            return this.sourceIterator.hasNext();
        }

        @Override
        public TTo next() {
            return this.converter.apply(this.sourceIterator.next());
        }
    }
}
