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

package com.scottlogic.deg.generator.generation.fieldvaluesources;

import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.common.util.FlatMappingSpliterator;
import com.scottlogic.deg.common.util.Defaults;
import com.scottlogic.deg.generator.restrictions.linear.NumericLimit;
import com.scottlogic.deg.generator.restrictions.linear.NumericRestrictions;
import com.scottlogic.deg.generator.utils.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.scottlogic.deg.generator.utils.SetUtils.stream;

public class RealNumberFieldValueSource implements FieldValueSource {
    private final BigDecimal inclusiveUpperLimit;
    private final BigDecimal inclusiveLowerLimit;
    private final BigDecimal stepSize;
    private final Set<BigDecimal> blacklist;
    private final int scale;
    private final static BigDecimal exclusivityAdjuster = BigDecimal.valueOf(Double.MIN_VALUE);

    public RealNumberFieldValueSource(
        NumericRestrictions restrictions,
        Set<Object> blacklist) {
        this.scale = restrictions.getNumericScale();
        this.stepSize = restrictions.getStepSize();

        NumericLimit lowerLimit = getLowerLimit(restrictions);

        this.inclusiveLowerLimit =
            (lowerLimit.isInclusive()
                ? lowerLimit.getValue()
                : lowerLimit.getValue().add(exclusivityAdjuster))
            .setScale(scale, RoundingMode.CEILING);

        NumericLimit upperLimit = getUpperLimit(restrictions);

        this.inclusiveUpperLimit =
            (upperLimit.isInclusive()
                ? upperLimit.getValue()
                : upperLimit.getValue().subtract(exclusivityAdjuster))
            .setScale(scale, RoundingMode.FLOOR);

        this.blacklist = blacklist.stream()
            .map(NumberUtils::coerceToBigDecimal)
            .filter(Objects::nonNull)
            .map(i -> i.setScale(scale, RoundingMode.HALF_UP))
            .filter(i -> this.inclusiveLowerLimit.compareTo(i) <= 0 && i.compareTo(this.inclusiveUpperLimit) <= 0)
            .collect(Collectors.toSet());
    }

    private NumericLimit getUpperLimit(NumericRestrictions restrictions) {
        BigDecimal maxValue = Defaults.NUMERIC_MAX;
        if (restrictions.getMax() == null) {
            return new NumericLimit(maxValue, true);
        }

        // Returns the smaller of the two maximum restrictions
        return new NumericLimit(maxValue.min(restrictions.getMax().getValue()), restrictions.getMax().isInclusive());
    }

    private NumericLimit getLowerLimit(NumericRestrictions restrictions) {
        BigDecimal minValue = Defaults.NUMERIC_MIN;
        if (restrictions.getMin() == null) {
            return new NumericLimit(minValue, true);
        }

        // Returns the larger of the two minimum restrictions
        return new NumericLimit(minValue.max(restrictions.getMin().getValue()), restrictions.getMin().isInclusive());
    }

    @Override
    public Stream<Object> generateInterestingValues() {
        return FlatMappingSpliterator.flatMap(
            Stream.of(
                streamOf(() -> new RealNumberIterator()).limit(1),
                streamOf(() -> new RealNumberIterator(new BigDecimal(0))).limit(1),
                streamOf(() -> new RealNumberIterator(inclusiveUpperLimit)).limit(1)
            ), Function.identity())
            .distinct();
    }

    @Override
    public Stream<Object> generateAllValues() {
        return stream(new RealNumberIterator());
    }

    @Override
    public Stream<Object> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return Stream.generate(() ->
            randomNumberGenerator.nextBigDecimal(
                inclusiveLowerLimit,
                inclusiveUpperLimit,
                scale))
            .filter(i -> !blacklist.contains(i))
            .map(Function.identity());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        RealNumberFieldValueSource otherSource = (RealNumberFieldValueSource) obj;
        return inclusiveUpperLimit.equals(otherSource.inclusiveUpperLimit) &&
            inclusiveLowerLimit.equals(otherSource.inclusiveLowerLimit) &&
            stepSize.equals(otherSource.stepSize) &&
            blacklist.equals(otherSource.blacklist) &&
            scale == otherSource.scale;
    }

    @Override
    public int hashCode() {
        return Objects.hash(inclusiveLowerLimit, inclusiveUpperLimit, stepSize, blacklist, scale);
    }

    private class RealNumberIterator implements Iterator<Object> {
        private BigDecimal nextValue;

        RealNumberIterator() {
            this(inclusiveLowerLimit); // we can say always exclusive because it will have been adjusted if not
        }

        RealNumberIterator(BigDecimal startingPoint) {
            if (startingPoint.compareTo(inclusiveLowerLimit) < 0) {
                startingPoint = inclusiveLowerLimit;
            }

            nextValue = startingPoint.setScale(scale);

            if (blacklist.contains(nextValue)) {
                next();
            }
        }

        @Override
        public boolean hasNext() {
            return nextValue.compareTo(inclusiveUpperLimit) <= 0;
        }

        @Override
        public BigDecimal next() {
            BigDecimal currentValue = nextValue;

            do {
                nextValue = nextValue.add(stepSize);
            } while (blacklist.contains(nextValue));

            return currentValue;
        }
    }

    private Stream<Object> streamOf(Iterable<Object> iterable){
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}