package com.scottlogic.deg.generator.restrictions.set;

import com.scottlogic.deg.generator.restrictions.MergeResult;
import com.scottlogic.deg.generator.restrictions.set.SetRestrictions;

import java.util.Optional;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class SetRestrictionsMerger {
    public MergeResult<SetRestrictions> merge(SetRestrictions left, SetRestrictions right) {
        if (left == null && right == null) {
            return new MergeResult<>(null);
        }
        return notNullOf(left).merge(notNullOf(right));
    }

    private SetRestrictions notNullOf(SetRestrictions setRestrictions) {
        return Optional.ofNullable(setRestrictions).orElse(new SetRestrictions(null, null));
    }
}
