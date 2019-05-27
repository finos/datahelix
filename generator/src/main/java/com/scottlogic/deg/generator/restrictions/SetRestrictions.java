package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.utils.SetUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SetRestrictions implements Restrictions {
    private static final SetRestrictions neutral = new SetRestrictions(null, null);

    private final Set<Object> whitelist;

    @NotNull
    private final Set<Object> blacklist;

    public Set<Object> getWhitelist() {
        return this.whitelist;
    }

    @NotNull
    public Set<Object> getBlacklist() {
        return this.blacklist;
    }

    protected SetRestrictions(
        Set<Object> whitelist,
        Set<Object> blacklist) {

        this.whitelist = whitelist;

        // normalise such that blacklist cannot be an empty set, to avoid ambiguity between null and {}
        this.blacklist = blacklist == null ? Collections.emptySet() : blacklist;
    }

    private boolean isEmpty(){
        return (this.whitelist == null || this.whitelist.isEmpty())
            && this.blacklist.isEmpty();
    }

    public static SetRestrictions allowNoValues() {
        return fromWhitelist(Collections.emptySet());
    }

    public static SetRestrictions fromWhitelist(@NotNull Set<Object> whitelist) {
        return new SetRestrictions(whitelist, null);
    }

    public static SetRestrictions fromBlacklist(@NotNull Set<Object> blacklist) {
        return new SetRestrictions(null, blacklist);
    }

    public static MergeResult<SetRestrictions> merge(SetRestrictions a, SetRestrictions b) {
        if (a == null && b == null)
            return new MergeResult<>(null);

        a = coalesce(a, neutral);
        b = coalesce(b, neutral);

        Set<Object> newWhitelist;
        if (a.whitelist == null && b.whitelist == null)
            newWhitelist = null;
        else if (a.whitelist == null)
            newWhitelist = b.whitelist;
        else if (b.whitelist == null)
            newWhitelist = a.whitelist;
        else
            newWhitelist = SetUtils.intersect(a.whitelist, b.whitelist);

        Set<Object> newBlacklist = SetUtils.union(
            coalesce(a.blacklist, Collections.emptySet()),
            coalesce(b.blacklist, Collections.emptySet()));

        if (newWhitelist != null && newBlacklist != null) {
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

        if (newWhitelist != null && newWhitelist.size() == 0) {
            return MergeResult.UNSUCCESSFUL;
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
