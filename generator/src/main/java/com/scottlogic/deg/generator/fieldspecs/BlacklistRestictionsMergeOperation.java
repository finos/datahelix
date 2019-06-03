package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.restrictions.BlacklistRestrictions;
import com.scottlogic.deg.generator.utils.SetUtils;

import java.util.Optional;

public class BlacklistRestictionsMergeOperation implements RestrictionMergeOperation {
    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        if (left.getBlacklistRestrictions() == null && right.getBlacklistRestrictions() == null)
            return Optional.of(merging);

        BlacklistRestrictions newBlacklist;
        if (left.getBlacklistRestrictions() == null)
            newBlacklist = right.getBlacklistRestrictions();
        else if (right.getBlacklistRestrictions() == null)
            newBlacklist = left.getBlacklistRestrictions();
        else
            newBlacklist = new BlacklistRestrictions(SetUtils.union(left.getBlacklistRestrictions().getBlacklist(), right.getBlacklistRestrictions().getBlacklist()));

        return Optional.of(merging.withBlacklistRestrictions(newBlacklist, FieldSpecSource.Empty));
    }
}
