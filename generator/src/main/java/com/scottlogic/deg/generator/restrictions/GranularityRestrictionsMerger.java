package com.scottlogic.deg.generator.restrictions;

public class GranularityRestrictionsMerger {

    public static GranularityRestrictions merge(GranularityRestrictions left, GranularityRestrictions right) {
        if (left == null || right == null) {
            return left == null ? right : left;
        }

        return new GranularityRestrictions(
            Math.min(left.getNumericScale(), right.getNumericScale()));
    }
}
