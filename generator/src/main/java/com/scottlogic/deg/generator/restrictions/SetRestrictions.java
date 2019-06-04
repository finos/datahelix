package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.utils.SetUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SetRestrictions implements Restrictions {
    private static final SetRestrictions neutral = new SetRestrictions(null);

    private final Set<Object> whitelist;

    public Set<Object> getWhitelist() {
        return this.whitelist;
    }

    public SetRestrictions(Set<Object> whitelist) {
        this.whitelist = whitelist;
    }

    private boolean isEmpty(){
        return (this.whitelist == null || this.whitelist.isEmpty());
    }

    public static SetRestrictions allowNoValues() {
        return fromWhitelist(Collections.emptySet());
    }

    public static SetRestrictions fromWhitelist(@NotNull Set<Object> whitelist) {
        return new SetRestrictions(whitelist);
    }

    @Override
    public String toString() {
        if (isEmpty())
            return "<empty>";

        return String.format(
            "IN %s",
            Objects.toString(whitelist));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SetRestrictions that = (SetRestrictions) o;
        return Objects.equals(whitelist, that.whitelist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(whitelist);
    }
}
