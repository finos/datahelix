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

public class NumericRestrictions extends LinearRestrictions<BigDecimal> {
    private static final int DEFAULT_NUMERIC_SCALE = 20;
    private final int numericScale;

    public NumericRestrictions(Limit<BigDecimal> min, Limit<BigDecimal> max){
        this(min, max, DEFAULT_NUMERIC_SCALE);
    }

    public NumericRestrictions(Limit<BigDecimal> min, Limit<BigDecimal> max, int numericScale){
        super(min, max, new NumericGranularity(numericScale), new NumericConverter());
        this.numericScale = numericScale;
    }

    public int getNumericScale() {
        return this.numericScale;
    }

    public BigDecimal getStepSize() {
        return BigDecimal.ONE.scaleByPowerOfTen(numericScale * -1);
    }


    @Override
    public String toString() {
        return String.format(
            "%s%s%s%s",
            getMin() != null ? "after " + getMin().toString() : "",
            getMin() != null && getMax() != null ? " and " : "",
            getMax() != null ? "before " + getMax().toString() : "",
            numericScale != 20 ? "granular-to " + numericScale : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumericRestrictions that = (NumericRestrictions) o;
        return Objects.equals(getMin(), that.getMin()) &&
            Objects.equals(getMax(), that.getMax()) &&
            Objects.equals(numericScale, that.numericScale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMin(), getMax(), numericScale);
    }
}
