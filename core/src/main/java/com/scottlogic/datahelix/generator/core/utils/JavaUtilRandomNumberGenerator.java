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

package com.scottlogic.datahelix.generator.core.utils;

import com.scottlogic.datahelix.generator.common.RandomNumberGenerator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

public class JavaUtilRandomNumberGenerator implements RandomNumberGenerator
{
    private final Random random;

    public JavaUtilRandomNumberGenerator(){
        random = new Random();
    }

    public JavaUtilRandomNumberGenerator(long seed){
        random = new Random(seed);
    }

    @Override
    public int nextInt() {
        return random.nextInt();
    }

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    @Override
    public long nextLong(long lowerInclusive, long upperExclusive) {
        long multiplier = (long) (random.nextDouble() * (double) (upperExclusive - lowerInclusive));
        return multiplier + lowerInclusive;
    }

    @Override
    public double nextDouble(double lowerInclusive, double upperExclusive) {
        return (random.nextDouble()
            * (upperExclusive - lowerInclusive))
            + lowerInclusive;
    }

    @Override
    public BigDecimal nextBigDecimal(BigDecimal lowerInclusive, BigDecimal upperExclusive) {
        int lowerScale = lowerInclusive.scale();
        int upperScale = upperExclusive.scale();
        int greatestScale = Math.max(lowerScale, upperScale);
        BigInteger lowerValue = adjustToScale(lowerInclusive, greatestScale);
        BigInteger upperValue = adjustToScale(upperExclusive, greatestScale);

        return new BigDecimal(nextBigInteger(lowerValue, upperValue), greatestScale);
    }

    private BigInteger adjustToScale(BigDecimal decimal, int scale) {
        return decimal.unscaledValue().multiply(BigInteger.TEN.pow(scale - decimal.scale()));
    }

    private BigInteger nextBigInteger(BigInteger lowerInclusive, BigInteger upperExclusive) {
        BigInteger range = upperExclusive.subtract(lowerInclusive);
        BigInteger randomValue = null;
        while (randomValue == null || randomValue.compareTo(range) >= 0) {
            randomValue = new BigInteger(range.bitLength(), random);
        }
        return lowerInclusive.add(randomValue);
    }

}
