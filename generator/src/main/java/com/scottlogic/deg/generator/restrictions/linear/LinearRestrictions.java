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

import com.scottlogic.deg.generator.restrictions.TypedRestrictions;

import java.math.BigDecimal;
import java.util.Objects;

public class LinearRestrictions<T extends Comparable<T>> implements TypedRestrictions {

    private final Limit<T> min;
    private final Limit<T> max;
    private final Granularity<T> granularity;
    private final Converter<T> converter;

    LinearRestrictions(Limit<T> min, Limit<T> max, Granularity<T> granularity, Converter<T> converter) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("linear restrictions cannot have null limits");
        }
        this.min = min;
        this.max = max;
        this.granularity = granularity;
        this.converter = converter;
    }

    @Override
    public boolean match(Object o){

        if (!isInstanceOf(o)) {
            return false;
        }

        T t = converter.convert(o);

        if (!min.isBefore(t)) {
            return false;
        }

        if (!max.isAfter(t)) {
            return false;
        }

        return granularity.isCorrectScale(t);
    }

    @Override
    public boolean isInstanceOf(Object o){
        return converter.isCorrectType(o);
    }

    public Limit<T> getMax() {
        return max;
    }

    public Limit<T> getMin() {
        return min;
    }

    public Converter<T> getConverter(){
        return converter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinearRestrictions<?> that = (LinearRestrictions<?>) o;
        return Objects.equals(min, that.min) &&
            Objects.equals(max, that.max) &&
            Objects.equals(granularity, that.granularity) &&
            Objects.equals(converter, that.converter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max, granularity, converter);
    }

    public Granularity<T> getGranularity(){
        return granularity;
    }
}
