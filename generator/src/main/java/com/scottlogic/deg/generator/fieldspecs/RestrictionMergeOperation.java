package com.scottlogic.deg.generator.fieldspecs;

import java.util.Optional;

public interface RestrictionMergeOperation {
    Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging);
}

