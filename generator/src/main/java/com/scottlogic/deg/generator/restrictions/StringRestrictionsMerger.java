package com.scottlogic.deg.generator.restrictions;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class StringRestrictionsMerger {
    public MergeResult<StringRestrictions> merge(StringRestrictions left, StringRestrictions right) {
        if (left == null && right == null)
            return new MergeResult<>(null);
        if (left == null)
            return new MergeResult<>(right);
        if (right == null)
            return new MergeResult<>(left);

        StringRestrictions mergedRestrictions = left.intersect(right);

        return mergedRestrictions.isContradictory()
            ? MergeResult.UNSUCCESSFUL
            : new MergeResult<>(mergedRestrictions);
    }
}

