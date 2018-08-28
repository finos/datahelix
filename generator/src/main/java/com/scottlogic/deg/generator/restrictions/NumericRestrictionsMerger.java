package com.scottlogic.deg.generator.restrictions;

import java.math.BigDecimal;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class NumericRestrictionsMerger {
    private enum MergeLimit {
        Min, Max
    }

    public NumericRestrictions merge(NumericRestrictions left, NumericRestrictions right) {
        if (left == null && right == null)
            return null;
        if (left == null)
            return right;
        if (right == null)
            return left;

        final NumericRestrictions merged = new NumericRestrictions();

        merged.min = getMergedLimitStructure(MergeLimit.Min, left.min, right.min);
        merged.max = getMergedLimitStructure(MergeLimit.Max, left.max, right.max);

        return merged;
    }

    private NumericLimit<BigDecimal> getMergedLimitStructure(MergeLimit mergeLimit, NumericLimit<BigDecimal> left, NumericLimit<BigDecimal> right) {
        if (left == null && right == null) {
            return null;
        }
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }

        if (left.getLimit().compareTo(right.getLimit()) == 0)
            return new NumericLimit<>(
                left.getLimit(),
                left.isInclusive() && right.isInclusive());
        switch(mergeLimit) {
            case Min:
                if (left.getLimit().compareTo(right.getLimit()) > 0)
                    return left;
                return right;
            case Max:
                if (left.getLimit().compareTo(right.getLimit()) < 0)
                    return left;
                return right;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
