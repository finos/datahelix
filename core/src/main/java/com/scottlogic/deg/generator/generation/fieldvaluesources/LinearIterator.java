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

package com.scottlogic.deg.generator.generation.fieldvaluesources;

import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;

import java.util.Iterator;

public class LinearIterator<T extends Comparable<T>> implements Iterator<T> {
    private final LinearRestrictions<T> linearRestrictions;
    private T next;
    private T current;

    public LinearIterator(LinearRestrictions<T> linearRestrictions) {
        this.linearRestrictions = linearRestrictions;
        next = linearRestrictions.getMin();
        current = next;
    }

    @Override
    public boolean hasNext() {
        return linearRestrictions.getMax().compareTo(next) >= 0
            && next.compareTo(current) >= 0;
    }

    @Override
    public T next() {
        current = next;
        next = linearRestrictions.getGranularity().getNext(next);
        return current;
    }
}
