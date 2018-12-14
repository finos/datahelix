package com.scottlogic.deg.generator.restrictions;

import java.math.BigDecimal;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class NumericRestrictionsMerger {
    private enum MergeLimit {
        MIN, MAX
    }

    public MergeResult<NumericRestrictions> merge(NumericRestrictions left, NumericRestrictions right) {
        if (left == null && right == null)
            return new MergeResult<>(null);
        if (left == null)
            return new MergeResult<>(right);
        if (right == null)
            return new MergeResult<>(left);

        final NumericRestrictions merged = new NumericRestrictions();


        merged.min = getMergedLimitStructure(MergeLimit.MIN, left.min, right.min);
        merged.max = getMergedLimitStructure(MergeLimit.MAX, left.max, right.max);

        if (merged.min != null && merged.max != null && merged.min.getLimit().compareTo(merged.max.getLimit()) >0 ) {
            return new MergeResult<>();
        }

        return new MergeResult<>(merged);
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
        switch (mergeLimit) {
            case MIN:
                if (left.getLimit().compareTo(right.getLimit()) > 0)
                    return left;
                return right;
            case MAX:
                if (left.getLimit().compareTo(right.getLimit()) < 0)
                    return left;
                return right;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
