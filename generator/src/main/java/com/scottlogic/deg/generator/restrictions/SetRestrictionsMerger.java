package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.restrictions.SetRestrictions;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class SetRestrictionsMerger {
    public SetRestrictions merge(SetRestrictions left, SetRestrictions right) {
        if (left == null && right == null)
            return null;
        if (left == null)
            return right;
        if (right == null)
            return left;

        final SetRestrictions merged = new SetRestrictions();
        merged.whitelist = getMergedSet(left.whitelist, right.whitelist, this::intersection);
        merged.blacklist = getMergedSet(left.blacklist, right.blacklist, this::union);

        return merged;
    }

    private <T> Set<?> getMergedSet(Set<?> left, Set<?> right, SetMerger setMerger) {
        if (left == null && right == null) {
            return null;
        }
        if (left == null) {
            return new HashSet<>(right);
        }
        if (right == null) {
            return new HashSet<>(left);
        }

        return setMerger.apply(left, right);
    }

    private Set<?> union(Set<?> left, Set<?> right) {
        return Stream.concat(left.stream(), right.stream()).collect(HashSet::new, HashSet::add, HashSet::addAll);
    }

    private Set<?> intersection(Set<?> left, Set<?> right) {
        final Set<?> intersection = new HashSet<>(left);
        intersection.retainAll(right);
        return intersection;
    }

    /**
     * A strategy used to make a set become more constraining
     */
    @FunctionalInterface
    interface SetMerger {
        Set<?> apply(Set<?> left, Set<?> right);
    }
}
