package com.scottlogic.deg.generator.restrictions.linear;

import com.scottlogic.deg.generator.restrictions.MergeResult;

public class LinearRestrictionsMerger {

    public static <T> MergeResult<LinearRestictions<T>> merge(LinearRestictions<T> left, LinearRestictions<T> right){
        if (left == null && right == null)
            return new MergeResult<>(null);
        if (left == null)
            return new MergeResult<>(right);
        if (right == null)
            return new MergeResult<>(left);

        Granularity<T> mergedGranularity = left.getGranularity().merge(right.getGranularity());

        Limit<T> mergedMin = getHighest(left.getMin(), right.getMin());
        Limit<T> mergedMax = getLowest(left.getMax(), right.getMax());

        LinearRestictions<T> mergedRestriction = new LinearRestictions<>(mergedMin, mergedMax, mergedGranularity, left.getType());

        if (isContradictory(mergedRestriction)) {
            return MergeResult.unsuccessful();
        }

        return new MergeResult<>(mergedRestriction);
    }

    private static <T> boolean isContradictory(LinearRestictions<T> restictions) {
        Limit<T> min = restictions.getMin();
        Limit<T> max = restictions.getMax();

        if (min.getValue().equals(max.getValue())){
            if (!min.isInclusive() || !max.isInclusive()){
                return true;
            }
        }

        return min.isAfter(max.getValue());
    }

    private static <T> Limit<T> getHighest(Limit<T> left, Limit<T> right) {
        if (left.getValue().equals(right.getValue())) {
            return getLeastInclusive(left, right);
        }
        return left.isBefore(right.getValue()) ? left : right;
    }

    private static <T> Limit<T> getLowest(Limit<T> left, Limit<T> right) {
        if (left.getValue().equals(right.getValue())) {
            return getLeastInclusive(left, right);
        }
        return left.isAfter(right.getValue()) ? left : right;
    }

    private static <T> Limit<T> getLeastInclusive(Limit<T> left, Limit<T> right) {
        return left.isInclusive() ? right : left;
    }
}
