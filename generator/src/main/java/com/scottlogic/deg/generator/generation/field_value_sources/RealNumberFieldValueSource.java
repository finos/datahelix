package com.scottlogic.deg.generator.generation.field_value_sources;

import com.scottlogic.deg.generator.restrictions.NumericLimit;
import com.scottlogic.deg.generator.utils.IRandomNumberGenerator;
import com.scottlogic.deg.generator.utils.NumberUtils;
import com.scottlogic.deg.generator.utils.UpCastingIterator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RealNumberFieldValueSource implements IFieldValueSource {
    private final BigDecimal upperLimit;
    private final BigDecimal lowerLimit;
    private final BigDecimal stepSize;
    private final Set<BigDecimal> blacklist;
    private final int scale;
    private final static BigDecimal exclusivityAdjuster = BigDecimal.valueOf(Double.MIN_VALUE);

    // TODO: Add comment about what scale parameter is (if we continue to use it)
    public RealNumberFieldValueSource(
        NumericLimit<BigDecimal> upperLimit,
        NumericLimit<BigDecimal> lowerLimit,
        Set<Object> blacklist,
        int scale) {
        // Using floor-based rounding for the limits, so with a step size of 10, we have steps of
        // [0-10), [10-20), [20-30) etc. We nudge the boundaries slightly to implement
        this.upperLimit = upperLimit.isInclusive()
            ? upperLimit.getLimit()
            : upperLimit.getLimit().subtract(exclusivityAdjuster);

        this.lowerLimit = lowerLimit.isInclusive()
            ? lowerLimit.getLimit().subtract(exclusivityAdjuster)
            : lowerLimit.getLimit();

        this.blacklist = blacklist.stream()
            .map(NumberUtils::coerceToBigDecimal)
            .filter(Objects::nonNull)
            .map(i -> i.setScale(scale, RoundingMode.HALF_UP)) // not sure if we should just dump any blacklist item that doesn't fit exactly on the scale
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
        return () -> new UpCastingIterator<>(
            Stream.of(
                streamOf(() -> new RealNumberIterator()).limit(2),
                streamOf(() -> new RealNumberIterator(new BigDecimal(0), true)).limit(1),
                streamOf(() -> new RealNumberIterator(upperLimit.subtract(stepSize), true)).limit(2))
            .flatMap(Function.identity())
            .distinct()
            .iterator());
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

        RealNumberIterator() {
            this(lowerLimit, false); // we can say always exclusive because it will have been adjusted if not
        }

        RealNumberIterator(BigDecimal startingPoint, boolean inclusive) {
            if (startingPoint.compareTo(lowerLimit) < 0)
                startingPoint = lowerLimit;
            else if (inclusive)
                startingPoint = startingPoint.subtract(exclusivityAdjuster);

            nextValue = startingPoint.setScale(scale, RoundingMode.FLOOR);
            next();
        }

        @Override
        public boolean hasNext() {
            return nextValue.compareTo(upperLimit) <= 0;
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