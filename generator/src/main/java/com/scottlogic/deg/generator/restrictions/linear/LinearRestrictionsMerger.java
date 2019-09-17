package com.scottlogic.deg.generator.restrictions.linear;

import com.scottlogic.deg.generator.restrictions.MergeResult;

public class LinearRestrictionsMerger {

    public <T> MergeResult<LinearRestrictions<T>> merge(LinearRestrictions<T> left, LinearRestrictions<T> right){
        if (left == null && right == null)
            return new MergeResult<>(null);
        if (left == null)
            return new MergeResult<>(right);
        if (right == null)
            return new MergeResult<>(left);

        Granularity<T> mergedGranularity = left.getGranularity().merge(right.getGranularity());

        Limit<T> mergedMin = getHighest(left.getMin(), right.getMin(), mergedGranularity);
        Limit<T> mergedMax = getLowest(left.getMax(), right.getMax());

        LinearRestrictions<T> mergedRestriction = new LinearRestrictions<>(mergedMin, mergedMax, mergedGranularity, left.getConverter());

        if (isContradictory(mergedRestriction)) {
            return MergeResult.unsuccessful();
        }

        return new MergeResult<>(mergedRestriction);
    }

    private static <T> boolean isContradictory(LinearRestrictions<T> restictions) {
        Limit<T> min = restictions.getMin();
        Limit<T> max = restictions.getMax();

        if (min == null || max == null){
            return false;
        }

        if (min.getValue().equals(max.getValue())){
            if (!min.isInclusive()){
                return true;
            }
            if (!max.isInclusive()){
                return true;
            }
        }

        return !min.isBefore(max.getValue());
    }

    private static <T> Limit<T> getHighest(Limit<T> left, Limit<T> right, Granularity<T> granularity ) { //TODO dry this code up
        if (left == null){
            return right;
        }
        if (right == null){
            return left;
        }

        if (left.getValue().equals(right.getValue())) {
            return getLeastInclusive(left, right);
        }
        return left.isAfter(right.getValue()) ? left : right;
    }

    private static <T> Limit<T> getLowest(Limit<T> left, Limit<T> right, Granularity<T> granularity) {
        if (left == null){
            return right;
        }
        if (right == null){
            return left;
        }

        if (left.getValue().equals(right.getValue())) {
            if(granularity.isCorrectScale(left.getValue())) {
                return getLeastInclusive(left, right);
            }
            return new Limit<T>()

        }
        return left.isBefore(right.getValue()) ? left : right;
    }

    private static <T> Limit<T> getLeastInclusive(Limit<T> left, Limit<T> right) {
        return left.isInclusive() ? right : left;
    }
}
