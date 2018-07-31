package com.scottlogic.deg.restriction;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class NumericRestrictionsMerger {
    private enum MergeLimit {
        Min, Max
    }

    public NumericRestrictions merge(NumericRestrictions left, NumericRestrictions right) {
        final NumericRestrictions merged = new NumericRestrictions();
        merged.min = getMergedLimit(MergeLimit.Min, left.min, right.min);
        merged.max = getMergedLimit(MergeLimit.Max, left.max, right.max);

        return merged;
    }

    private <T extends Number & Comparable<T>> T getMergedLimit(MergeLimit mergeLimit, T left, T right) {
        if (left == null && right == null) {
            return null;
        }
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }

        switch(mergeLimit) {
            case Min:
                if (left.compareTo(right) > 0) return right;
                break;
            case Max:
                if (left.compareTo(right) < 0) return right;
                break;
            default:
                throw new UnsupportedOperationException();
        }

        return left;
    }
}
