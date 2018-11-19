package com.scottlogic.deg.generator.restrictions;

public class GranularityRestrictions {
    private final int numericScale;

    public GranularityRestrictions(ParsedGranularity granularity) {
        this.numericScale = granularity.getNumericGranularity().scale();
    }

    private GranularityRestrictions(int numericScale) {
        this.numericScale = numericScale;
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
}
