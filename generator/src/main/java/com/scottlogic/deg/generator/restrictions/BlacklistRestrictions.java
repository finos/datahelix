package com.scottlogic.deg.generator.restrictions;

import java.util.Set;

public class BlacklistRestrictions implements TypedRestrictions {
    private final Set<Object> blacklist;

    public BlacklistRestrictions(Set<Object> blacklist) {
        this.blacklist = blacklist;
    }

    public Set<Object> getBlacklist() {
        return blacklist;
    }

    @Override
    public boolean match(Object o) {
        return !blacklist.contains(o);
    }

    @Override
    public boolean isInstanceOf(Object o) {
        return true;
    }

    @Override
    public String toString() {
        return String.format("NOT %s", blacklist);
    }
}
