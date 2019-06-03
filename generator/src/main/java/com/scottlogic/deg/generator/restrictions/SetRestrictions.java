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

    protected SetRestrictions(Set<Object> whitelist) {
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

    public static MergeResult<SetRestrictions> merge(SetRestrictions a, SetRestrictions b) {
        if (a == null && b == null)
            return new MergeResult<>(null);

        a = a != null ? a : neutral;
        b = b != null ? b : neutral;

        Set<Object> newWhitelist;
        if (a.whitelist == null && b.whitelist == null)
            newWhitelist = null;
        else if (a.whitelist == null)
            newWhitelist = b.whitelist;
        else if (b.whitelist == null)
            newWhitelist = a.whitelist;
        else
            newWhitelist = SetUtils.intersect(a.whitelist, b.whitelist);

        if (newWhitelist != null && newWhitelist.size() == 0) {
            return MergeResult.unsuccessful();
        }

        return new MergeResult<>(new SetRestrictions(newWhitelist));
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
