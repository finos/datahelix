package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.LinearFieldValueSource;
import com.scottlogic.deg.generator.restrictions.StringRestrictions;
import com.scottlogic.deg.generator.restrictions.TypedRestrictions;
import com.scottlogic.deg.generator.restrictions.linear.LinearRestrictions;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class RestrictionsFieldSpec extends BaseFieldSpec {
    private final TypedRestrictions restrictions;
    private final Set<Object> blacklist;

    RestrictionsFieldSpec(TypedRestrictions restrictions, boolean nullable, Set<Object> blacklist) {
        super(nullable);
        this.restrictions = restrictions;
        this.blacklist = blacklist;
    }

    @Override
    public boolean permits(Object value) {
        return !blacklist.contains(value) && restrictions.match(value);
    }

    @Override
    public FieldValueSource getFieldValueSource() {
        return appendNullSource(
            restrictions.createFieldValueSource(blacklist));
    }

    @Override
    public FieldSpec withNotNull() {
        return new RestrictionsFieldSpec(restrictions, false, blacklist);
    }


    public TypedRestrictions getRestrictions() {
        return restrictions;
    }

    public Collection<Object> getBlacklist() {
        return blacklist;
    }

    public RestrictionsFieldSpec withBlacklist(Set<Object> blacklist) {
        return new RestrictionsFieldSpec(restrictions, nullable, blacklist);
    }

    @Override
    public String toString() {
        return String.format("%s%s",
            nullable ? " " : " Not Null ",
            restrictions == null ? "" : restrictions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestrictionsFieldSpec that = (RestrictionsFieldSpec) o;
        return Objects.equals(restrictions, that.restrictions) &&
            Objects.equals(blacklist, that.blacklist) &&
            Objects.equals(nullable, that.nullable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restrictions, blacklist, nullable);
    }
}
