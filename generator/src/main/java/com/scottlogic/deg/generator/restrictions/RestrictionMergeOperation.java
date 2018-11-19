package com.scottlogic.deg.generator.restrictions;

public interface RestrictionMergeOperation {
    boolean successful(FieldSpec left, FieldSpec right, FieldSpec merged);
}

