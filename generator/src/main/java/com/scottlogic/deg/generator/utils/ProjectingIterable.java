/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.generator.utils;

import java.util.Iterator;
import java.util.function.Function;

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
            TFrom next = sourceIterator.next();
            return converter.apply(next);
        }
    }
}
