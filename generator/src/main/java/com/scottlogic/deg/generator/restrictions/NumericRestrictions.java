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

package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.restrictions.linear.NumericLimit;

import java.math.BigDecimal;
import java.util.Objects;

import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.NUMERIC;

public class NumericRestrictions implements TypedRestrictions {
    public static final int DEFAULT_NUMERIC_SCALE = 20;
    private final int numericScale;
    private final NumericLimit min;
    private final NumericLimit max;

    public NumericRestrictions(NumericLimit min, NumericLimit max){
        this(min, max, DEFAULT_NUMERIC_SCALE);
    }

    public NumericRestrictions(NumericLimit min, NumericLimit max, int numericScale){
        this.min = min;
        this.max = max;
        this.numericScale = numericScale;
    }

    public int getNumericScale() {
        return this.numericScale;
    }

    @Override
    public boolean match(Object o) {
        if (!isInstanceOf(o)) {
            return false;
        }

        BigDecimal n = new BigDecimal(o.toString());

        if(getMin() != null){
            if(n.compareTo(getMin().getValue()) < (getMin().isInclusive() ? 0 : 1))
            {
                return false;
            }
        }

        if(getMax() != null){
            if(n.compareTo(getMax().getValue()) > (getMax().isInclusive() ? 0 : -1))
            {
                return false;
            }
        }

        return isCorrectScale(n);
    }

    @Override
    public boolean isInstanceOf(Object o) {
        return NUMERIC.isInstanceOf(o);
    }

    public BigDecimal getStepSize() {
        return BigDecimal.ONE.scaleByPowerOfTen(numericScale * -1);
    }

    private boolean isCorrectScale(BigDecimal inputNumber) {
        return inputNumber.stripTrailingZeros().scale() <= numericScale;
    }

    @Override
    public String toString() {
        return String.format(
            "%s%s%s%s",
            getMin() != null ? getMin().toString(">") : "",
            getMin() != null && getMax() != null ? " and " : "",
            getMax() != null ? getMax().toString("<") : "",
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

    public NumericLimit getMin() {
        return min;
    }

    public NumericLimit getMax() {
        return max;
    }
}
