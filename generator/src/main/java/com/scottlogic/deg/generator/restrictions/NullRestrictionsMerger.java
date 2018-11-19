package com.scottlogic.deg.generator.restrictions;

import com.sun.scenario.effect.Merge;

import java.util.Optional;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class NullRestrictionsMerger {
    public MergeResult<NullRestrictions> merge(NullRestrictions left, NullRestrictions right) {
        if (left == null && right == null)
            return new MergeResult<>(null);

        final NullRestrictions merged = new NullRestrictions();
        Optional<NullRestrictions.Nullness> nullness = getMergedNullness(getNullness(left), getNullness(right));
        if (!nullness.isPresent()){
            return new MergeResult<>();
        }

        merged.nullness = nullness.get();
        return new MergeResult<>(merged);
    }

    private Optional<NullRestrictions.Nullness> getMergedNullness(NullRestrictions.Nullness left, NullRestrictions.Nullness right) {
        if (left == null && right == null) {
            return Optional.of(null);
        }
        if (left == null) {
            return Optional.of(right);
        }
        if (right == null) {
            return Optional.of(left);
        }

        if (left == right) {
            return Optional.of(left);
        }

        return Optional.empty();
    }

    private NullRestrictions.Nullness getNullness(NullRestrictions restrictions) {
        return restrictions != null ? restrictions.nullness : null;
    }
}
