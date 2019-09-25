package com.scottlogic.deg.generator.restrictions;

public interface RestrictionsMerger<T extends TypedRestrictions> {
    MergeResult<T> merge(T left, T right);
}
