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

package com.scottlogic.deg.generator.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class JavaUtilRandomNumberGenerator implements RandomNumberGenerator {
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
    public int nextInt(int lowerInclusive, int upperExclusive) {
        // implementation copied from Random::internalNextInt
        if (lowerInclusive < upperExclusive) {
            int n = upperExclusive - lowerInclusive;
            if (n > 0) {
                return nextInt(n) + lowerInclusive;
            }
            else {  // range not representable as int
                int r;
                do {
                    r = nextInt();
                } while (r < lowerInclusive || r >= upperExclusive);
                return r;
            }
        }
        else {
            return nextInt();
        }
    }

    @Override
    public double nextDouble(double lowerInclusive, double upperExclusive) {
        return random.nextDouble()
            * (upperExclusive - lowerInclusive)
            + lowerInclusive;
    }

    @Override
    public BigDecimal nextBigDecimal(BigDecimal lowerInclusive, BigDecimal upperExclusive) {
        return new BigDecimal(random.nextDouble())
            .multiply(upperExclusive.subtract(lowerInclusive))
            .add(lowerInclusive);
    }
}
