package com.scottlogic.deg.restriction;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class SetRestrictionsMerger {
    public SetRestrictions merge(SetRestrictions left, SetRestrictions right) {
        final SetRestrictions merged = new SetRestrictions();
        merged.whitelist = getMergedSet(left.whitelist, right.whitelist);
        merged.blacklist = getMergedSet(left.blacklist, right.blacklist);

        return merged;
    }

    private Set<?> getMergedSet(Set<?> left, Set<?> right) {
        if (left == null && right == null) {
            return null;
        }
        if (left == null) {
            return new HashSet<>(right);
        }
        if (right == null) {
            return new HashSet<>(left);
        }
        return Stream.concat(left.stream(), right.stream()).collect(HashSet::new, HashSet::add, HashSet::addAll);
    }
}
