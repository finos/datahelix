package com.scottlogic.deg.generator.restrictions;

public interface RestrictionsMerger {
    MergeResult<TypedRestrictions> merge(TypedRestrictions left, TypedRestrictions right);
}
