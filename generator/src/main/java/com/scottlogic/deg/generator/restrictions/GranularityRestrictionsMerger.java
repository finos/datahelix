package com.scottlogic.deg.generator.restrictions;

public class GranularityRestrictionsMerger {

    public GranularityRestrictions merge(GranularityRestrictions left, GranularityRestrictions right) {
        if (left == null || right == null) {
            return left == null ? right : left;
        }

        return GranularityRestrictions.merge(left, right);
    }
}
