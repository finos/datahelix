package com.scottlogic.deg.generator.fieldspecs;

public interface RestrictionMergeOperation {
    FieldSpec applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merging);
}

