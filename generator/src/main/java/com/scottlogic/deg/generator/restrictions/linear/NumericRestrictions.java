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

    public static final NumericLimit MIN_LIMIT = new NumericLimit(NUMERIC_MIN, true);
    public static final NumericLimit MAX_LIMIT = new NumericLimit(NUMERIC_MAX, true);

    public NumericRestrictions(Limit<BigDecimal> min, Limit<BigDecimal> max){
        this(min, max, DEFAULT_NUMERIC_SCALE);
    }

    public NumericRestrictions(Limit<BigDecimal> min, Limit<BigDecimal> max, int numericScale){
        super(
            min != null ? min : MIN_LIMIT,
            max != null ? max : MAX_LIMIT,
            new NumericGranularity(numericScale),
            new NumericConverter()
        );
    }

    public int getNumericScale() {
        return ((NumericGranularity)getGranularity()).getDecimalPlaces();
    }

    public BigDecimal getStepSize() {
        return BigDecimal.ONE.scaleByPowerOfTen(getNumericScale() * -1);
    }

    @Override
    public String toString() {
        return String.format(
            "%s%s%s%s",
            getMin() != null ? "after " + getMin().toString() : "",
            getMin() != null && getMax() != null ? " and " : "",
            getMax() != null ? "before " + getMax().toString() : "",
            getNumericScale() != 20 ? "granular-to " + getNumericScale() : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumericRestrictions that = (NumericRestrictions) o;
        return Objects.equals(getMin(), that.getMin()) &&
            Objects.equals(getMax(), that.getMax()) &&
            Objects.equals(getNumericScale(), that.getNumericScale());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMin(), getMax(), getNumericScale());
    }
}
