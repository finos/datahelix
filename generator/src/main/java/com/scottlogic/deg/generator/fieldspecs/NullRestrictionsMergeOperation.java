package com.scottlogic.deg.generator.fieldspecs;


import java.util.Optional;

public class
NullRestrictionsMergeOperation implements RestrictionMergeOperation {

    @Override
    public Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging) {
        if (left.isNullable() && right.isNullable()) {
            return Optional.of(merging);
        }

        return Optional.of(merging.withNotNull());
    }
}

