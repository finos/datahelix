package com.scottlogic.deg.generator.restrictions.linear;

import com.scottlogic.deg.generator.restrictions.MergeResult;

public class LinearRestrictionsMerger<T extends Comparable<T>> {

    public MergeResult<LinearRestrictions<T>> merge(LinearRestrictions<T> left, LinearRestrictions<T> right){
        if (left == null && right == null)
            return new MergeResult<>(null);
        if (left == null)
            return new MergeResult<>(right);
        if (right == null)
            return new MergeResult<>(left);

        Granularity<T> mergedGranularity = left.getGranularity().merge(right.getGranularity());

        Limit<T> mergedMin = getHighest(left.getMin(), right.getMin(), mergedGranularity);
        Limit<T> mergedMax = getLowest(left.getMax(), right.getMax(), mergedGranularity);

        LinearRestrictions<T> mergedRestriction = new LinearRestrictions<>(mergedMin, mergedMax, mergedGranularity, left.getConverter());

        if (isContradictory(mergedRestriction)) {
            return MergeResult.unsuccessful();
        }

        return new MergeResult<>(mergedRestriction);
    }

    private boolean isContradictory(LinearRestrictions<T> restictions) {
        T inclusiveMinValue = restictions.getMin().isInclusive()
            ? restictions.getMin().getValue()
            : restictions.getGranularity().getNext(restictions.getMin().getValue());

        return !restictions.getMax().isAfter(inclusiveMinValue);
    }

    private Limit<T> getHighest(Limit<T> left, Limit<T> right, Granularity<T> granularity) {
        if (left.getValue().equals(right.getValue())) {
            return getLeastInclusive(left, right);
        }

        return roundUpToNextValidValue(
            left.isAfter(right.getValue()) ? left : right,
            granularity
        );
    }

    private Limit<T> getLowest(Limit<T> left, Limit<T> right, Granularity<T> granularity) {
        if (left.getValue().equals(right.getValue())) {
            return getLeastInclusive(left, right);
        }
        return roundDownToNextValidValue(
            left.isBefore(right.getValue()) ? left : right,
            granularity
        );
    }

    private Limit<T> roundUpToNextValidValue(Limit<T> limit, Granularity<T> granularity) {
        if (granularity.isCorrectScale(limit.getValue())){
            return limit;
        }
        else {
            return new Limit<>(granularity.getNext(granularity.trimToGranularity(limit.getValue())), true);
        }
    }

    private Limit<T> roundDownToNextValidValue(Limit<T> limit, Granularity<T> granularity) {
        if (granularity.isCorrectScale(limit.getValue())){
            return limit;
        }
        else {
            return new Limit<>(granularity.trimToGranularity(limit.getValue()), true);
        }
    }

    private Limit<T> getLeastInclusive(Limit<T> left, Limit<T> right) {
        return left.isInclusive() ? right : left;
    }
}
