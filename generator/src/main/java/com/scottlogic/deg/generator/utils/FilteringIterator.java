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
import java.util.function.Predicate;

public class FilteringIterator<T> implements Iterator<T> {
    private final Iterator<T> underlyingIterator;

    private T nextValueToReturn;
    private boolean haveCompletedIteration;

    private Predicate<T> predicate;

    public FilteringIterator(Iterator<T> underlyingIterator, Predicate<T> predicate) {
        this.underlyingIterator = underlyingIterator;
        this.predicate = predicate;

        this.searchForNextValidValue();
    }

    private void searchForNextValidValue() {
        while (this.underlyingIterator.hasNext()) {
            T nextValue = this.underlyingIterator.next();
            if (this.predicate.test(nextValue)) {
                this.nextValueToReturn = nextValue;
                return;
            }
        }

        this.haveCompletedIteration = true;
    }

    @Override
    public boolean hasNext() {
        return !this.haveCompletedIteration;
    }

    @Override
    public T next() {
        T valueToReturn = this.nextValueToReturn;
        this.searchForNextValidValue();
        return valueToReturn;
    }
}
