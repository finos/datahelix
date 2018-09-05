package com.scottlogic.deg.generator.generation.field_value_sources;

import com.scottlogic.deg.generator.utils.IRandomNumberGenerator;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Iterator;

public class RealNumberFieldValueSource implements IFieldValueSource {
    private final BigDecimal upperLimit;
    private final BigDecimal lowerLimit;
    private final BigDecimal stepSize;
    private final int scale;

    public RealNumberFieldValueSource(
        BigDecimal upperLimit,
        BigDecimal lowerLimit,
        int scale) {
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;

        this.scale = scale;
        this.stepSize = new BigDecimal("1").scaleByPowerOfTen(scale * -1);
    }

    @Override
    public boolean isFinite() {
        return true;
    }

    @Override
    public long getValueCount() {
        long innerSteps = upperLimit
            .subtract(lowerLimit)
            .divideToIntegralValue(stepSize, new MathContext(1, RoundingMode.FLOOR))
            .longValue();

        return isOnStep(upperLimit) || isOnStep(lowerLimit)
            ? innerSteps + 1
            : innerSteps;
    }

    private boolean isOnStep(BigDecimal value)
    {
        return value.setScale(scale, RoundingMode.CEILING).compareTo(value) == 0;
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
        private BigDecimal nextValue = lowerLimit.setScale(scale, RoundingMode.HALF_UP);

        @Override
        public boolean hasNext() {
            return nextValue.compareTo(upperLimit) != 1;
        }

        @Override
        public BigDecimal next() {
            BigDecimal currentValue = nextValue;

            nextValue = nextValue.add(stepSize);

            return currentValue;
        }
    }
}
