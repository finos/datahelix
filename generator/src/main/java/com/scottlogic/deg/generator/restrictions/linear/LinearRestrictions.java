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

import com.scottlogic.deg.common.profile.constraintdetail.Granularity;
import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.LinearFieldValueSource;
import com.scottlogic.deg.generator.restrictions.TypedRestrictions;

import java.util.Objects;
import java.util.Set;

public class LinearRestrictions<T extends Comparable<T>> implements TypedRestrictions<T> {
    private final T min;
    private final T max;
    private final Granularity<T> granularity;

    public LinearRestrictions(T inclusiveMin, T inclusiveMax, Granularity<T> granularity) {
        this.min = inclusiveMin;
        this.max = inclusiveMax;
        this.granularity = granularity;
    }

    @Override
    public boolean match(T o){
        if (min.compareTo(o) > 0) {
            return false;
        }

        if (max.compareTo(o) < 0) {
            return false;
        }

        return granularity.isCorrectScale(o);
    }

    @Override
    public FieldValueSource<T> createFieldValueSource(Set<T> blacklist) {
        return new LinearFieldValueSource<>(this, blacklist);
    }

    public T getMax() {
        return max;
    }

    public T getMin() {
        return min;
    }

    public Granularity<T> getGranularity(){
        return granularity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinearRestrictions<?> that = (LinearRestrictions<?>) o;
        return Objects.equals(min, that.min) &&
            Objects.equals(max, that.max) &&
            Objects.equals(granularity, that.granularity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max, granularity);
    }

    @Override
    public String toString() {
        return "min=" + getMin() + ", max=" + getMax() + " " + getGranularity().toString();
    }
}
