package com.scottlogic.deg.generator.restrictions;

public class DateTimeRestrictionsMerger {
    private enum MergeLimit {
        MIN, MAX
    }

    public DateTimeRestrictions merge(DateTimeRestrictions left, DateTimeRestrictions right) {
        if (left == null && right == null)
            return null;
        if (left == null)
            return right;
        if (right == null)
            return left;

        final DateTimeRestrictions merged = new DateTimeRestrictions();

        merged.min = getMergedLimitStructure(MergeLimit.MIN, left.min, right.min);
        merged.max = getMergedLimitStructure(MergeLimit.MAX, left.max, right.max);

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
