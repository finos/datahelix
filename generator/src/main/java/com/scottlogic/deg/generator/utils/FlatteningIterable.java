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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * Iterable that decorates an underlying iterable where each item maps to a list of items, where the iterable of lists is flattened into a single iterable.
 */
public class FlatteningIterable<TFrom, TTo> implements Iterable<TTo> {
    private final Iterable<TFrom> underlyingIterable;
    private final Function<TFrom, List<TTo>> converter;

    public FlatteningIterable(
            Iterable<TFrom> underlyingIterable,
            Function<TFrom, List<TTo>> converter) {

        this.underlyingIterable = underlyingIterable;
        this.converter = converter;
    }

    @Override
    public Iterator<TTo> iterator() {
        return new InternalIterator(
                this.underlyingIterable.iterator(),
                this.converter);
    }

    class InternalIterator implements Iterator<TTo> {
        private final Iterator<TFrom> sourceIterator;
        private final Function<TFrom, List<TTo>> converter;
        private List<TTo> currentItemList;

        InternalIterator(Iterator<TFrom> sourceIterator, Function<TFrom, List<TTo>> converter) {
            currentItemList = new ArrayList<>();
            this.sourceIterator = sourceIterator;
            this.converter = converter;
            refreshItemList();
        }

        @Override
        public boolean hasNext() {
            return !currentItemList.isEmpty();
        }

        @Override
        public TTo next() {
            if (currentItemList.isEmpty()) {
                throw new NoSuchElementException();
            }
            final TTo nextItem = currentItemList.remove(currentItemList.size() - 1);
            refreshItemList();
            return nextItem;
        }

        private void refreshItemList() {
            while (currentItemList.isEmpty() && sourceIterator.hasNext()) {
                final TFrom nextSourceItem = sourceIterator.next();
                currentItemList.addAll(converter.apply(nextSourceItem));
            }
        }
    }
}
