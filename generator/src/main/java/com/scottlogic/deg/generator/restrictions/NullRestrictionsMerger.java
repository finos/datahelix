package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.restrictions.NullRestrictions;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class NullRestrictionsMerger {
    public NullRestrictions merge(NullRestrictions left, NullRestrictions right) {
        if (left == null && right == null)
            return null;

        final NullRestrictions merged = new NullRestrictions();
        merged.nullness = getMergedNullness(getNullness(left), getNullness(right));

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

        throw new UnmergeableRestrictionException();
    }

    private NullRestrictions.Nullness getNullness(NullRestrictions restrictions) {
        return restrictions != null ? restrictions.nullness : null;
    }
}
