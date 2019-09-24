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

package com.scottlogic.deg.generator.restrictions.linear;

import java.util.Objects;

public class Limit<T extends Comparable<? super T>> {

    private final T limit;
    private final boolean isInclusive;

    public Limit(T limit, boolean isInclusive) {
        this.limit = limit;
        this.isInclusive = isInclusive;
    }

    public T getValue() {
        return limit;
    }

    public boolean isInclusive() {
        return isInclusive;
    }

    public boolean isBefore(T other) {
        if (isInclusive){
            return limit.compareTo(other) <= 0;
        }
        return limit.compareTo(other) < 0;
    }

    public boolean isAfter(T other) {
        if (isInclusive){
            return limit.compareTo(other) >= 0;
        }
        return limit.compareTo(other) > 0;
    }

    public String toString() {
        return String.format(
            "%s%s",
            limit.toString(),
            isInclusive ? " inclusive" : ""
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Limit<?> limit1 = (Limit<?>) o;
        return isInclusive == limit1.isInclusive &&
            Objects.equals(limit, limit1.limit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, isInclusive);
    }
}
