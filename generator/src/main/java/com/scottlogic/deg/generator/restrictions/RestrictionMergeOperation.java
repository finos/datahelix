package com.scottlogic.deg.generator.restrictions;

import java.util.Optional;

public interface RestrictionMergeOperation {
    Optional<FieldSpec> applyMergeOperation(FieldSpec left, FieldSpec right, FieldSpec merged);
}

