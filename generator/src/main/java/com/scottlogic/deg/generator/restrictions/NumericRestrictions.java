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

import com.scottlogic.deg.common.profile.Types;

import java.math.BigDecimal;
import java.util.Objects;

public class NumericRestrictions extends AbstractTypedRestrictions {
    public static final int DEFAULT_NUMERIC_SCALE = 20;
    private final int numericScale;
    public NumericLimit<BigDecimal> min;
    public NumericLimit<BigDecimal> max;

    public NumericRestrictions(){
        numericScale = DEFAULT_NUMERIC_SCALE;
    }

    public NumericRestrictions(int numericScale){
        this.numericScale = numericScale;
    }

    public int getNumericScale() {
        return this.numericScale;
    }

    @Override
    protected Types getType() {
        return Types.NUMERIC;
    }

    @Override
    public boolean match(Object o) {
        if (!isInstanceOf(o)) {
            return false;
        }

        BigDecimal n = new BigDecimal(o.toString());

        if(min != null){
            if(n.compareTo(min.getLimit()) < (min.isInclusive() ? 0 : 1))
            {
                return false;
            }
        }

        if(max != null){
            if(n.compareTo(max.getLimit()) > (max.isInclusive() ? 0 : -1))
            {
                return false;
            }
        }

        return isCorrectScale(n);
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
            min != null ? min.toString(">") : "",
            min != null && max != null ? " and " : "",
            max != null ? max.toString("<") : "",
            numericScale != 20 ? "granular-to " + numericScale : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumericRestrictions that = (NumericRestrictions) o;
        return Objects.equals(min, that.min) &&
            Objects.equals(max, that.max) &&
            Objects.equals(numericScale, that.numericScale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max, numericScale);
    }
}
