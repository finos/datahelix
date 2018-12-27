package com.scottlogic.deg.generator.restrictions;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MustContainRestrictionMerger {
    public MustContainRestriction merge(MustContainRestriction left, MustContainRestriction right) {
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }

        return new MustContainRestriction(
            Stream.concat(
                left.getRequiredObjects().stream(),
                right.getRequiredObjects().stream()
            ).collect(Collectors.toSet())
        );
    }
}
