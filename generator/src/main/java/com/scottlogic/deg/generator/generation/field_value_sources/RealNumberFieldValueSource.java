package com.scottlogic.deg.generator.generation.field_value_sources;

import com.scottlogic.deg.generator.restrictions.NumericLimit;
import com.scottlogic.deg.generator.utils.IRandomNumberGenerator;
import com.scottlogic.deg.generator.utils.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class RealNumberFieldValueSource implements IFieldValueSource {
    private final BigDecimal upperLimit;
    private final BigDecimal lowerLimit;
    private final BigDecimal stepSize;
    private final Set<BigDecimal> blacklist;
    private final int scale;

    // TODO: Add comment about what scale parameter is (if we continue to use it)
    public RealNumberFieldValueSource(
        NumericLimit<BigDecimal> upperLimit,
        NumericLimit<BigDecimal> lowerLimit,
        Set<Object> blacklist,
        int scale) {
        BigDecimal exclusivityAdjuster = BigDecimal.valueOf(Double.MIN_VALUE);

        // Using floor rounding, so with a step size of 10, we have steps of
        // [0-10), [10-20), [20-30) etc. Nudge the boundaries slightly to implement
        this.upperLimit = upperLimit.isInclusive()
            ? upperLimit.getLimit()
            : upperLimit.getLimit().subtract(exclusivityAdjuster);

        this.lowerLimit = lowerLimit.isInclusive()
            ? lowerLimit.getLimit().subtract(exclusivityAdjuster)
            : lowerLimit.getLimit();

        this.blacklist = blacklist.stream()
            .map(NumberUtils::coerceToBigDecimal)
            .filter(Objects::nonNull)
            .map(i -> i.setScale(scale, RoundingMode.HALF_UP))
            .filter(i -> this.lowerLimit.compareTo(i) <= 0 && i.compareTo(this.upperLimit) <= 0)
            .collect(Collectors.toSet());

        this.scale = scale;
        this.stepSize = new BigDecimal("1").scaleByPowerOfTen(scale * -1);
    }

    @Override
    public boolean isFinite() {
        return true;
    }

    @Override
    public long getValueCount() {
        BigDecimal upperStep = upperLimit.divide(stepSize, 0, RoundingMode.FLOOR);
        BigDecimal lowerStep = lowerLimit.divide(stepSize, 0, RoundingMode.FLOOR);

        return upperStep.subtract(lowerStep).longValue() - blacklist.size();
    }

    @Override
    public Iterable<Object> generateInterestingValues() {
        return null;
    }

    @Override
    public Iterable<Object> generateAllValues() {
        return RealNumberIterator::new;
    }

    @Override
    public Iterable<Object> generateRandomValues(IRandomNumberGenerator randomNumberGenerator) {
        return null;
    }

    private class RealNumberIterator implements Iterator<Object> {
        private BigDecimal nextValue;

        public RealNumberIterator() {
            nextValue = lowerLimit.setScale(scale, RoundingMode.FLOOR);
            next();
        }

        @Override
        public boolean hasNext() {
            return nextValue.compareTo(upperLimit) != 1;
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
}