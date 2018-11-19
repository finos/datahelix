package com.scottlogic.deg.generator.restrictions;

public interface RestrictionMergeOperation {
    boolean applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merged);
}

