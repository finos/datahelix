package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.utils.SetUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SetRestrictions {
    private static final SetRestrictions neutral = new SetRestrictions();

    private final Set<Object> whitelist;
    private final Set<Object> blacklist;

    public Set<Object> getWhitelist() {
        return this.whitelist;
    }

    public Set<Object> getBlacklist() {
        return this.blacklist;
    }

    private SetRestrictions(){
        this.whitelist = new HashSet<>();
        this.blacklist = new HashSet<>();
    }

    public SetRestrictions(Set<Object> whitelist, Set<Object> blacklist) {
        this.whitelist = whitelist;
        this.blacklist = blacklist;
    }

    public boolean isEmpty(){
        return ((this.whitelist == null || this.whitelist.isEmpty())
            && (this.blacklist == null || this.blacklist.isEmpty()));
    }

    public static SetRestrictions fromWhitelist(Set<Object> whitelist) {
        if (whitelist == null) {
            throw new IllegalArgumentException("The whitelist cannot be null");
        }

        return new SetRestrictions(whitelist, Collections.emptySet());
    }

    public static SetRestrictions fromBlacklist(Set<Object> blacklist) {
        if (blacklist == null) {
            throw new IllegalArgumentException("The blacklist cannot be null");
        }

        return new SetRestrictions(Collections.emptySet(), blacklist);
    }

    public static MergeResult<SetRestrictions> merge(SetRestrictions a, SetRestrictions b) {
        if (a == null && b == null)
            return new MergeResult<>(null);

        a = coalesce(a, neutral);
        b = coalesce(b, neutral);

        Set<Object> newWhitelist;
        if (a.whitelist.isEmpty() && b.whitelist.isEmpty())
            newWhitelist = Collections.emptySet();
        else if (a.whitelist.isEmpty())
            newWhitelist = b.whitelist;
        else if (b.whitelist.isEmpty())
            newWhitelist = a.whitelist;
        else
            newWhitelist = SetUtils.intersect(a.whitelist, b.whitelist);

        Set<Object> newBlacklist = SetUtils.union(a.blacklist, b.blacklist);

        if (!newWhitelist.isEmpty() && !newBlacklist.isEmpty()) {
            Set<Object> whiteAndBlacklistIntersection = SetUtils.intersect(
                newBlacklist,
                newWhitelist);

            if (whiteAndBlacklistIntersection.size() > 0) {
                newWhitelist = newWhitelist.stream()
                    .filter(val -> !whiteAndBlacklistIntersection.contains(val))
                    .collect(Collectors.toSet());
            }

            newBlacklist = Collections.emptySet();
        }

        if (newWhitelist.isEmpty() && newBlacklist.isEmpty()) {
            return new MergeResult<>();
        }

        return new MergeResult<>(new SetRestrictions(newWhitelist, newBlacklist));
    }

    private static <T> T coalesce(T preferred, T fallback) {
        return preferred != null ? preferred : fallback;
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "<empty>";

        if (whitelist.isEmpty())
            return String.format(
                    "NOT IN %s",
                    Objects.toString(blacklist));

        if (blacklist.isEmpty())
            return String.format(
                    "IN %s",
                    Objects.toString(whitelist));

        return String.format(
            "IN %s AND NOT IN %s",
            Objects.toString(whitelist, "-"),
            Objects.toString(blacklist, "-"));
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
