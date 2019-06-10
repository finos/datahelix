package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.restrictions.MergeResult;
import com.scottlogic.deg.generator.restrictions.NullRestrictions;
import com.scottlogic.deg.generator.restrictions.NullRestrictionsMerger;

import java.util.Optional;

public class NullRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final NullRestrictionsMerger nullRestrictionsMerger = new NullRestrictionsMerger();

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        MergeResult<NullRestrictions> mergeResult = nullRestrictionsMerger.merge(
            left.getNullRestrictions(),
            right.getNullRestrictions());

        if (!mergeResult.successful){
            return Optional.empty();
        }

        return Optional.of(merging.withNullRestrictions(
            mergeResult.restrictions,
            FieldSpecSource.fromFieldSpecs(left, right)));
    }
}

