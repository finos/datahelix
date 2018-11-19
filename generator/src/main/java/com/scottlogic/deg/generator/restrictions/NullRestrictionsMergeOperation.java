package com.scottlogic.deg.generator.restrictions;

import java.util.Optional;

public class NullRestrictionsMergeOperation implements RestrictionMergeOperation {
    private static final NullRestrictionsMerger nullRestrictionsMerger = new NullRestrictionsMerger();

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merged) {
        MergeResult<NullRestrictions> mergeResult = nullRestrictionsMerger.merge(
            left.getNullRestrictions(),
            right.getNullRestrictions());

        if (!mergeResult.successful){
            return Optional.empty();
        }

        return Optional.of(merged.setNullRestrictions(mergeResult.restrictions));
    }
}

