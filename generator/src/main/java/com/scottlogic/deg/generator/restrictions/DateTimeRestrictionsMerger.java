package com.scottlogic.deg.generator.restrictions;

public class DateTimeRestrictionsMerger {
    private enum MergeLimit {
        MIN, MAX
    }

    public MergeResult<DateTimeRestrictions> merge(DateTimeRestrictions left, DateTimeRestrictions right) {
        if (left == null && right == null)
            return new MergeResult<>(null);
        if (left == null)
            return new MergeResult<>(right);
        if (right == null)
            return new MergeResult<>(left);

        final DateTimeRestrictions merged = new DateTimeRestrictions();

        merged.min = getMergedLimitStructure(MergeLimit.MIN, left.min, right.min);
        merged.max = getMergedLimitStructure(MergeLimit.MAX, left.max, right.max);

        if (merged.min == null || merged.max == null){
            return new MergeResult<>(merged);
        }

        if (merged.min.isAfter(merged.max)) {
            return new MergeResult<>();
        }

        return new MergeResult<>(merged);
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
