package com.scottlogic.deg.generator.restrictions;

import java.math.BigDecimal;
import java.util.Objects;

import static com.scottlogic.deg.generator.utils.NumberUtils.coerceToBigDecimal;

public class GranularityRestrictions {
    private final int numericScale;

    public GranularityRestrictions(ParsedGranularity granularity) {
        this.numericScale = granularity.getNumericGranularity().scale();
    }

    private GranularityRestrictions(int numericScale) {
        this.numericScale = numericScale;
    }

    public static boolean isCorrectScale(Number inputNumber, double granularity) {
        BigDecimal inputAsBigDecimal = coerceToBigDecimal(inputNumber);
        return inputAsBigDecimal.scale() <= granularity;
    }

    public int getNumericScale() {
        return this.numericScale;
    }

    public static GranularityRestrictions merge(GranularityRestrictions left, GranularityRestrictions right) {
        if (left == null || right == null) {
            return left == null ? right : left;
        }

        return new GranularityRestrictions(
            Math.min(left.numericScale, right.numericScale));
    }

    @Override
    public String toString() {
        return String.format("granular-to %d", this.numericScale);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GranularityRestrictions that = (GranularityRestrictions) o;
        return numericScale == that.numericScale;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numericScale);
    }
}
