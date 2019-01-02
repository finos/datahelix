package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.restrictions.MustContainRestrictionMerger;

import java.util.Optional;

public class MustContainRestrictionMergeOperation implements RestrictionMergeOperation {
    private final MustContainRestrictionMerger merger = new MustContainRestrictionMerger();

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merged) {
        return Optional.of(merged.withMustContainRestriction(
            merger.merge(left.getMustContainRestriction(), right.getMustContainRestriction())));
    }
}
