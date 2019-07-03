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

public class RestartableIterator<T> implements Iterator<T> {
    private final List<T> cache = new ArrayList<>();
    private int index = 0;
    private final Iterator<T> underlyingIterator;

    public RestartableIterator(Iterator<T> underlyingIterator) {
        this.underlyingIterator = underlyingIterator;
    }

    @Override
    public boolean hasNext() {
        return index < cache.size() || underlyingIterator.hasNext();
    }

    @Override
    public T next() {
        T item;
        if (index < cache.size()) {
            item = cache.get(index);
        } else {
            item = underlyingIterator.next();
            cache.add(item);
        }

        index++;

        return item;
    }

    public void restart() {
        index = 0;
    }
}
