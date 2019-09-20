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

import com.scottlogic.deg.common.util.Defaults;

import java.math.BigDecimal;
import java.util.Objects;

public class NumericLimit implements Limit<BigDecimal> {
    private final BigDecimal limit;
    private final boolean isInclusive;

    public NumericLimit(BigDecimal limit, boolean isInclusive) {
        this.limit = capLimit(limit);
        this.isInclusive = isInclusive;
    }

    private BigDecimal capLimit(BigDecimal limit) {
        if (limit.compareTo(Defaults.NUMERIC_MAX) > 0) {
            return Defaults.NUMERIC_MAX;
        } else if (limit.compareTo(Defaults.NUMERIC_MIN) < 0){
            return Defaults.NUMERIC_MIN;
        }
        return limit;
    }

    public BigDecimal getValue() {
        return limit;
    }

    public boolean isInclusive() {
        return isInclusive;
    }

    @Override
    public boolean isBefore(BigDecimal other) {
        if (isInclusive){
            return limit.compareTo(other) <= 0;
        }
        return limit.compareTo(other) < 0;
    }

    @Override
    public boolean isAfter(BigDecimal other) {
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
        NumericLimit that = (NumericLimit) o;
        return isInclusive == that.isInclusive &&
            Objects.equals(limit, that.limit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, isInclusive);
    }
}
