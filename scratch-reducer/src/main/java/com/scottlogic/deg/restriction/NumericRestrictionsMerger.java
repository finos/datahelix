package com.scottlogic.deg.restriction;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class NumericRestrictionsMerger {
    public NumericRestrictions<?> merge(NumericRestrictions<?> left, NumericRestrictions<?> right) {
        if (left.getTypeToken().isAssignableFrom(right.getTypeToken())) {
            throw new IllegalStateException();
        }
        if (right.getTypeToken().isAssignableFrom(left.getTypeToken())) {
            throw new IllegalStateException();
        }
        final NumericRestrictions<?> merged = left.constructReified();
        merged.min = getMergedMin(left.min, right.min);
        merged.max = getMergedMax(left.max, right.max);

        return merged;
    }

    private <T extends Number & Comparable<T>> T getMergedMin(T left, T right) {
        if (left == null && right == null) {
            return null;
        }
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }

        if (left.compareTo(right) > 0) {
            return right;
        }
        return left;
    }

    private <T extends Number & Comparable<T>> T getMergedMax(T left, T right) {
        if (left == null && right == null) {
            return null;
        }
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }

        if (left.compareTo(right) < 0) {
            return right;
        }
        return left;
    }
}
