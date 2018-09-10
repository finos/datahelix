package com.scottlogic.deg.generator.generation.field_value_sources;

import com.scottlogic.deg.generator.restrictions.NumericLimit;
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

public class RealNumberFieldValueSource implements IFieldValueSource {
    private final BigDecimal inclusiveUpperLimit;
    private final BigDecimal inclusiveLowerLimit;
    private final BigDecimal stepSize;
    private final Set<BigDecimal> blacklist;
    private final int scale;
    private final static BigDecimal exclusivityAdjuster = BigDecimal.valueOf(Double.MIN_VALUE);

    /**
     * @param scale The granularity of the output values: the number of digits to the right of the decimal point. See BigDecimal.scale() for details
     */
    public RealNumberFieldValueSource(
        NumericLimit<BigDecimal> lowerLimit,
        NumericLimit<BigDecimal> upperLimit,
        Set<Object> blacklist,
        int scale) {
        this.scale = scale;
        this.stepSize = new BigDecimal("1").scaleByPowerOfTen(scale * -1);

        if (lowerLimit == null)
            lowerLimit = new NumericLimit<>(BigDecimal.valueOf(Double.MAX_VALUE).negate(), true);

        this.inclusiveLowerLimit =
            (lowerLimit.isInclusive()
                ? lowerLimit.getLimit()
                : lowerLimit.getLimit().add(exclusivityAdjuster))
            .setScale(scale, RoundingMode.CEILING);

        if (upperLimit == null)
            upperLimit = new NumericLimit<>(BigDecimal.valueOf(Double.MAX_VALUE), true);

        this.inclusiveUpperLimit =
            (upperLimit.isInclusive()
                ? upperLimit.getLimit()
                : upperLimit.getLimit().subtract(exclusivityAdjuster))
            .setScale(scale, RoundingMode.FLOOR);

        this.blacklist = blacklist.stream()
            .map(NumberUtils::coerceToBigDecimal)
            .filter(Objects::nonNull)
            .map(i -> i.setScale(scale, RoundingMode.HALF_UP))
            .filter(i -> this.inclusiveLowerLimit.compareTo(i) <= 0 && i.compareTo(this.inclusiveUpperLimit) <= 0)
            .collect(Collectors.toSet());
    }

    @Override
    public boolean isFinite() {
        return true;
    }

    @Override
    public long getValueCount() {
        BigDecimal lowerStep = inclusiveLowerLimit.divide(stepSize, 0, RoundingMode.HALF_UP);
        BigDecimal upperStep = inclusiveUpperLimit.divide(stepSize, 0, RoundingMode.HALF_UP);

        return upperStep.subtract(lowerStep).longValue() + 1 - blacklist.size();
    }

    @Override
    public Iterable<Object> generateInterestingValues() {
        return () -> new UpCastingIterator<>(
            Stream.of(
                streamOf(() -> new RealNumberIterator()).limit(2),
                streamOf(() -> new RealNumberIterator(new BigDecimal(0))).limit(1),
                streamOf(() -> new RealNumberIterator(inclusiveUpperLimit.subtract(stepSize))).limit(2))
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
        return () -> new UpCastingIterator<>(
            new FilteringIterator<>(
                new SupplierBasedIterator<>(() ->
                    new BigDecimal(
                        randomNumberGenerator.nextDouble(
                            inclusiveLowerLimit.doubleValue(),
                            inclusiveUpperLimit.doubleValue()
                        )).setScale(scale, RoundingMode.HALF_UP)),
                i -> !blacklist.contains(i)));
    }

    private class RealNumberIterator implements Iterator<Object> {
        private BigDecimal nextValue;

        RealNumberIterator() {
            this(inclusiveLowerLimit); // we can say always exclusive because it will have been adjusted if not
        }

        RealNumberIterator(BigDecimal startingPoint) {
            if (startingPoint.compareTo(inclusiveLowerLimit) < 0)
                startingPoint = inclusiveLowerLimit;

            nextValue = startingPoint.setScale(scale);

            if (blacklist.contains(nextValue))
                next();
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