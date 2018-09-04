package com.scottlogic.deg.generator.generation.field_value_sources;

import com.scottlogic.deg.generator.utils.IRandomNumberGenerator;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Iterator;

public class RealNumberFieldValueSource implements IFieldValueSource {
    private final BigDecimal upperLimit;
    private final BigDecimal lowerLimit;
    private final int scale;
    private final BigDecimal stepSize;

    public RealNumberFieldValueSource(
        BigDecimal upperLimit,
        BigDecimal lowerLimit,
        int scale) {
        this.upperLimit = upperLimit.setScale(scale, BigDecimal.ROUND_HALF_UP);
        this.lowerLimit = lowerLimit.setScale(scale, BigDecimal.ROUND_HALF_UP);
        this.scale = scale;
        this.stepSize = new BigDecimal("1").scaleByPowerOfTen(scale * -1);
    }

    @Override
    public boolean isFinite() {
        return true;
    }

    @Override
    public long getValueCount() {
        return upperLimit
            .subtract(lowerLimit)
            .divideToIntegralValue(stepSize, new MathContext(1, RoundingMode.FLOOR))
            .longValue() + 1;
    }

    @Override
    public Iterable<Object> generateInterestingValues() {
        return null;
    }

    // perhaps we can changed IndexedSupplier to pass in the previous value as an arg rather than the index!
    @Override
    public Iterable<Object> generateAllValues() {
        return RealNumberIterator::new;
    }

    @Override
    public Iterable<Object> generateRandomValues(IRandomNumberGenerator randomNumberGenerator) {
        return null;
    }

    private class RealNumberIterator implements Iterator<Object> {
        private BigDecimal nextValue = lowerLimit;

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
