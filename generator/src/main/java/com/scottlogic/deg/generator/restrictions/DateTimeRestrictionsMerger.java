package com.scottlogic.deg.generator.restrictions;

public class DateTimeRestrictionsMerger {
    private enum MergeLimit {
        Min, Max
    }

    public DateTimeRestrictions merge(DateTimeRestrictions left, DateTimeRestrictions right) {
        if (left == null && right == null)
            return null;
        if (left == null)
            return right;
        if (right == null)
            return left;

        final DateTimeRestrictions merged = new DateTimeRestrictions();

        merged.min = getMergedLimitStructure(MergeLimit.Min, left.min, right.min);
        merged.max = getMergedLimitStructure(MergeLimit.Max, left.max, right.max);

        return merged;
    }

    private DateTimeRestrictions.DateTimeLimit getMergedLimitStructure(
            MergeLimit mergeLimit,
            DateTimeRestrictions.DateTimeLimit left,
            DateTimeRestrictions.DateTimeLimit right) {
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
            return new DateTimeRestrictions.DateTimeLimit(left.getLimit(), left.isInclusive() && right.isInclusive());
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
