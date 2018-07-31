package com.scottlogic.deg.restriction;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class NullRestrictionsMerger {
    public NullRestrictions merge(NullRestrictions left, NullRestrictions right) {
        final NullRestrictions merged = new NullRestrictions();
        merged.nullness = getMergedNullness(left.nullness, right.nullness);

        return merged;
    }

    private NullRestrictions.Nullness getMergedNullness(NullRestrictions.Nullness left, NullRestrictions.Nullness right) {
        if (left == null && right == null) {
            return null;
        }
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }

        if (left == right) {
            return left;
        }

        throw new UnsupportedOperationException();
    }
}
