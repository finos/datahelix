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

package com.scottlogic.deg.common.profile.constraintdetail;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class NumericGranularity implements Granularity<BigDecimal> {

    private final int decimalPlaces;

    public NumericGranularity(int decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    @Override
    public boolean isCorrectScale(BigDecimal value) {
        return value.stripTrailingZeros().scale() <= decimalPlaces;
    }

    @Override
    public NumericGranularity merge(Granularity<BigDecimal> otherGranularity) {
        NumericGranularity other = (NumericGranularity) otherGranularity;
        return decimalPlaces <= other.decimalPlaces ? this : other;
    }

    @Override
    public BigDecimal getNext(BigDecimal value, int amount) {
        BigDecimal addAmount = BigDecimal.ONE.scaleByPowerOfTen(decimalPlaces * -1)
            .multiply(BigDecimal.valueOf(amount));
        return value.add(addAmount);
    }

    @Override
    public BigDecimal getNext(BigDecimal value) {
        return value.add(BigDecimal.ONE.scaleByPowerOfTen(decimalPlaces * -1));
    }

    @Override
    public BigDecimal getRandom(BigDecimal min, BigDecimal max, RandomNumberGenerator randomNumberGenerator) {
        BigDecimal value = randomNumberGenerator.nextBigDecimal(max, max);
        return trimToGranularity(value);
    }

    @Override
    public BigDecimal getPrevious(BigDecimal value) {
        if (!isCorrectScale(value)){
            return trimToGranularity(value);
        }

        return value.subtract(BigDecimal.ONE.scaleByPowerOfTen(decimalPlaces * -1));
    }

    @Override
    public BigDecimal trimToGranularity(BigDecimal value) {
        return value.setScale(decimalPlaces, RoundingMode.FLOOR);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumericGranularity that = (NumericGranularity) o;
        return decimalPlaces == that.decimalPlaces;
    }

    @Override
    public String toString() {
        return decimalPlaces +
            " decimal places=";
    }

    @Override
    public int hashCode() {
        return Objects.hash(decimalPlaces);
    }
}
