package com.scottlogic.deg.generator.restrictions;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class SetRestrictions implements Restrictions {
    @NotNull
    private final Set<Object> whitelist;

    public Set<Object> getWhitelist() {
        return this.whitelist;
    }

    public SetRestrictions(Set<Object> whitelist) {
        this.whitelist = whitelist;
    }

    public static SetRestrictions allowNoValues() {
        return fromWhitelist(Collections.emptySet());
    }

    public static SetRestrictions fromWhitelist(Set<Object> whitelist) {
        return new SetRestrictions(whitelist);
    }

    @Override
    public String toString() {
        if (whitelist.isEmpty()) {
            return "<No Values>";
        }
        return String.format("IN %s", whitelist);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SetRestrictions that = (SetRestrictions) o;
        return Objects.equals(whitelist, that.whitelist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(whitelist);
    }
}
