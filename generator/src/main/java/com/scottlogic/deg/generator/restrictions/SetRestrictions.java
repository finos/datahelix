package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.utils.SetUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SetRestrictions {
    private static final SetRestrictions neutral = new SetRestrictions(
        null,
        new HashSet<>());

    private final Set<Object> whitelist;
    private final Set<Object> blacklist;

    public Set<Object> getWhitelist() {
        return this.whitelist;
    }

    public Set<Object> getBlacklist() {
        return this.blacklist;
    }

    public SetRestrictions(Set<Object> whitelist, Set<Object> blacklist) {
        this.whitelist = whitelist;
        this.blacklist = blacklist;
    }

    public static SetRestrictions fromWhitelist(Set<Object> whitelist) {
        return new SetRestrictions(whitelist, null);
    }

    public static SetRestrictions fromBlacklist(Set<Object> blacklist) {
        return new SetRestrictions(null, blacklist);
    }

    public static SetRestrictions merge(SetRestrictions a, SetRestrictions b) {
        if (a == null && b == null)
            return null;

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

        return new SetRestrictions(newWhitelist, newBlacklist);
    }

    private static <T> T coalesce(T preferred, T fallback) {
        return preferred != null ? preferred : fallback;
    }

    @Override
    public String toString() {
        return String.format(
            "wList: %s, bList: %s",
            Objects.toString(whitelist, "-"),
            Objects.toString(blacklist, "-"));
    }
}
