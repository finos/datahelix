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

        int granularity = Math.min(left.getNumericScale(), right.getNumericScale());
        final NumericRestrictions merged = new NumericRestrictions(granularity);

        merged.min = getMergedLimitStructure(MergeLimit.MIN, left.min, right.min);
        merged.max = getMergedLimitStructure(MergeLimit.MAX, left.max, right.max);

        if (!canEmitSomeNumericValues(merged)){
            return MergeResult.UNSUCCESSFUL;
        }

        return new MergeResult<>(merged);
    }

    private boolean canEmitSomeNumericValues(NumericRestrictions merged) {
        NumericLimit<BigDecimal> min = merged.min;
        NumericLimit<BigDecimal> max = merged.max;

        if (min == null || max == null){
            return true; //no constraints
        }


        if (min.isInclusive() && max.isInclusive()){
            return isLessThanOrEqualTo(min, max);
        }

        if (!granularityIsWithinRange(merged)){
            return false;
        }

        return isLessThan(min, max);
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
                if (!isLessThan(left, right))
                    return left;
                return right;
            case MAX:
                if (isLessThan(left, right))
                    return left;
                return right;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private boolean isLessThan(NumericLimit<BigDecimal> min, NumericLimit<BigDecimal> max) {
        return min.getLimit().compareTo(max.getLimit()) < 0;
    }

    private boolean isLessThanOrEqualTo(NumericLimit<BigDecimal> min, NumericLimit<BigDecimal> max) {
        return min.getLimit().compareTo(max.getLimit()) <= 0;
    }

    private boolean granularityIsWithinRange(NumericRestrictions merged) {
        if (!merged.min.isInclusive()){
            NumericLimit<BigDecimal> nextNumber = new NumericLimit<>(
                merged.min.getLimit().add(merged.getStepSize()), true);

            if (merged.max.isInclusive()){
                return isLessThanOrEqualTo(nextNumber, merged.max);
            }
            return isLessThan(nextNumber, merged.max);
        }


        if (!merged.max.isInclusive()){
            NumericLimit<BigDecimal> nextNumber = new NumericLimit<>(
                merged.max.getLimit().subtract(merged.getStepSize()), true);

            if (merged.min.isInclusive()){
                return isLessThanOrEqualTo(merged.min, nextNumber);
            }
            return isLessThan(merged.min, nextNumber);
        }

        return false;
    }

}
