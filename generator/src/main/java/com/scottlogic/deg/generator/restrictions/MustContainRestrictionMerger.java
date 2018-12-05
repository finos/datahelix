package com.scottlogic.deg.generator.restrictions;

public class MustContainRestrictionMerger {
    public MustContainRestriction merge(MustContainRestriction left, MustContainRestriction right) {
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }

        // TODO: implement merging
        return right;
    }
}
