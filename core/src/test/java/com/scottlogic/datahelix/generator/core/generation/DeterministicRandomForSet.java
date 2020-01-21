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

package com.scottlogic.datahelix.generator.core.generation;

import com.scottlogic.datahelix.generator.common.RandomNumberGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

class DeterministicRandomForSet implements RandomNumberGenerator {
    private static final int randomIndexForNull = 1;

    private final boolean yieldNull;
    private final AtomicBoolean nullHasBeenReturned = new AtomicBoolean(false);
    private final Queue<Integer> values;
    private final int max;

    public DeterministicRandomForSet(List<Integer> valuesInOrder, boolean yieldNull) {
        this.values = new LinkedList<>(valuesInOrder);
        this.yieldNull = yieldNull;
        this.max = values.size() - 1;
    }

    @Override
    public int nextInt() {
        Integer next = values.poll();

        if (next == null) {
            throw new RuntimeException("Random overflow");
        }

        return next;
    }

    @Override
    public int nextInt(int bound) {
        boolean requestingNull = bound == 4;

        if (this.yieldNull && !this.nullHasBeenReturned.get() && requestingNull) {
            nullHasBeenReturned.set(true);
            return randomIndexForNull;
        } else if (this.yieldNull && this.nullHasBeenReturned.get() && requestingNull) {
            return randomIndexForNull - 1; //anything that <> randomIndexForNull
        }

        return nextInt();
    }

    @Override
    public long nextLong(long lowerInclusive, long upperInclusive) {
        return (long)nextDouble(lowerInclusive, upperInclusive);
    }

    @Override
    public double nextDouble(double lowerInclusive, double upperExclusive) {
        int nextInt = nextInt();
        double range = upperExclusive - lowerInclusive;
        double randomInRange = ((range / max) * nextInt);
        return randomInRange + lowerInclusive;
    }

    @Override
    public BigDecimal nextBigDecimal(BigDecimal lowerInclusive, BigDecimal upperExclusive) {
        int nextInt = nextInt();
        BigDecimal range = upperExclusive.subtract(lowerInclusive);
        BigDecimal randomInRange = range
            .divide(BigDecimal.valueOf(max), RoundingMode.UNNECESSARY)
            .multiply(BigDecimal.valueOf(nextInt));
        return randomInRange.add(lowerInclusive);
    }
}
