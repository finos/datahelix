package com.scottlogic.deg.generator.restrictions.set;

import com.scottlogic.deg.generator.restrictions.MergeResult;
import com.scottlogic.deg.generator.restrictions.Restrictions;
import com.scottlogic.deg.generator.utils.SetUtils;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SetRestrictions implements Restrictions {

    /**
     * The whitelist permits:
     * Empty = Empty set
     * Some = Restricted set
     * Full = Universal set
     *
     * Because the universal set (full) is infeasible in terms of memory, we also represent
     * the universal set with null.
     */
    private final Set<Object> whitelist;

    /**
     * The blacklist permits:
     * Empty = Universal set
     * Some = Restricted set
     * Full = Empty set
     *
     * The null blacklist would be equivalent to the empty set, so we enforce that we should
     * always have an instance (of at least the empty set)
     */
    private final Set<Object> blacklist;

    public Optional<Set<Object>> getWhitelist() {
        return Optional.ofNullable(whitelist);
    }

    public Set<Object> getBlacklist() {
        return this.blacklist;
    }

    protected SetRestrictions(Set<Object> whitelist, Set<Object> blacklist) {
        this.whitelist = whitelist;
        this.blacklist = blacklist == null ? Collections.emptySet() : blacklist;
    }

    private boolean isEmpty() {
        return (whitelist == null || whitelist.isEmpty()) && blacklist.isEmpty();
    }

    public static SetRestrictions allowNoValues() {
        return fromWhitelist(Collections.emptySet());
    }

    public static SetRestrictions fromWhitelist(Set<Object> whitelist) {
        return new SetRestrictions(whitelist, null);
    }

    public static SetRestrictions fromBlacklist(Set<Object> blacklist) {
        return new SetRestrictions(null, blacklist);
    }

    private Set<Object> mergeWhitelist(Set<Object> left, Set<Object> right) {
        if (left == null && right == null) {
            return null;
        } else if (left == null) {
            return right;
        } else if (right == null) {
            return left;
        } else {
            return SetUtils.intersect(left, right);
        }
    }

    private Set<Object> mergeBlackList(Set<Object> left, Set<Object> right) {
        return SetUtils.union(getOrEmpty(left), getOrEmpty(right));
    }

    private Set<Object> getOrEmpty(Set<Object> set) {
        return Optional.ofNullable(set).orElse(Collections.emptySet());
    }

    public MergeResult<SetRestrictions> merge(SetRestrictions other) {
        Set<Object> newWhitelist = mergeWhitelist(whitelist, other.whitelist);

        Set<Object> newBlacklist = mergeBlackList(blacklist, other.blacklist);

        if (newWhitelist != null && newBlacklist != null) {
            Set<Object> whiteAndBlacklistIntersection = SetUtils.intersect(
                newBlacklist,
                newWhitelist);

            if (!whiteAndBlacklistIntersection.isEmpty()) {
                newWhitelist = newWhitelist.stream()
                    .filter(val -> !whiteAndBlacklistIntersection.contains(val))
                    .collect(Collectors.toSet());
            }

            newBlacklist = Collections.emptySet();
        }

        if (newWhitelist != null && newWhitelist.isEmpty()) {
            return MergeResult.unsuccessful();
        }

        return new MergeResult<>(new SetRestrictions(newWhitelist, newBlacklist));
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "<empty>";

        if (whitelist == null || whitelist.isEmpty())
            return String.format(
                "NOT IN %s",
                Objects.toString(blacklist));

        if (blacklist.isEmpty())
            return String.format(
                "IN %s",
                Objects.toString(whitelist));

        throw new RuntimeException("SetRestrictions is in illegal state (both whitelist and blacklist populated)");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SetRestrictions that = (SetRestrictions) o;
        return Objects.equals(whitelist, that.whitelist) &&
            Objects.equals(blacklist, that.blacklist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(whitelist, blacklist);
    }
}
