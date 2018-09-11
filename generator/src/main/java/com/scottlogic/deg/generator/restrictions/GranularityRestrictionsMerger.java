package com.scottlogic.deg.generator.restrictions;

public class GranularityRestrictionsMerger {

    public GranularityRestrictions merge(GranularityRestrictions left, GranularityRestrictions right) {
        if (left == null || right == null) {
            return left == null ? right : left;
        }

        GranularityRestrictions mergedRestrictions = new GranularityRestrictions();
        mergedRestrictions.numericScale = mergeNumericScale(left.numericScale, right.numericScale);

        return mergedRestrictions;
    }

    private int mergeNumericScale(int leftNumericScale, int rightNumericScale) {
        return Math.min(leftNumericScale, rightNumericScale);
    }
}
