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

import java.math.BigDecimal;
import java.util.Objects;

import static com.scottlogic.deg.common.util.Defaults.*;

public class NumericRestrictions extends LinearRestrictions<BigDecimal> {

    public static final Limit<BigDecimal> NUMERIC_MIN_LIMIT = new Limit<>(NUMERIC_MIN, true);
    public static final Limit<BigDecimal> NUMERIC_MAX_LIMIT = new Limit<>(NUMERIC_MAX, true);
    public static final NumericConverter CONVERTER = new NumericConverter();

    public NumericRestrictions(Limit<BigDecimal> min, Limit<BigDecimal> max){
        this(min, max, DEFAULT_NUMERIC_SCALE);
    }

    public NumericRestrictions(Limit<BigDecimal> min, Limit<BigDecimal> max, int numericScale){
        this(min, max, new NumericGranularity(numericScale));
    }
    public NumericRestrictions(Limit<BigDecimal> min, Limit<BigDecimal> max, Granularity<BigDecimal> granularity){
        super(capMin(min), capMax(max), granularity, CONVERTER);
    }

    private static Limit<BigDecimal> capMax(Limit<BigDecimal> max) {
        if (max.isAfter(NUMERIC_MAX)) {
            return new Limit<>(NUMERIC_MAX, true);
        } else if (!max.isAfter(NUMERIC_MIN)) {
            return new Limit<>(NUMERIC_MIN, false);
        } else {
            return max;
        }
    }

    private static Limit<BigDecimal> capMin(Limit<BigDecimal> min) {
        if (min.isBefore(NUMERIC_MIN)) {
            return new Limit<>(NUMERIC_MIN, true);
        } else if (!min.isBefore(NUMERIC_MAX)) {
            return new Limit<>(NUMERIC_MAX, false);
        } else {
            return min;
        }
    }


    @Override
    public String toString() {
        return
            "after " + getMin().toString() +
            " and " +
            "before " + getMax().toString() +
            getGranularity().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumericRestrictions that = (NumericRestrictions) o;
        return Objects.equals(getMin(), that.getMin()) &&
            Objects.equals(getMax(), that.getMax()) &&
            Objects.equals(getGranularity(), that.getGranularity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMin(), getMax(), getGranularity());
    }


}
