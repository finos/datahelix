package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.generation.fieldvaluesources.CannedValuesFieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;

import java.util.Objects;

public class WhitelistFieldSpec extends FieldSpec {

    private final DistributedList<Object> whitelist;

    WhitelistFieldSpec(DistributedList<Object> whitelist, boolean nullable) {
        super(nullable);
        if (whitelist.isEmpty()){
            throw new UnsupportedOperationException("cannot create with empty whitelist");
        }
        this.whitelist = whitelist;
    }

    @Override
    public boolean permits(Object value) {
        if (!whitelist.list().contains(value)) {
            return false;
        }

        return true;
    }

    @Override
    public FieldValueSource getFieldValueSource() {
        return appendNullSource(new CannedValuesFieldValueSource(whitelist));
    }

    @Override
    public WhitelistFieldSpec withNotNull() {
        return new WhitelistFieldSpec(whitelist, false);
    }

    public DistributedList<Object> getWhitelist() {
        return whitelist;
    }

    @Override
    public String toString() {
        if (whitelist.isEmpty()) {
            return "Null only";
        }
        return (nullable ? "" : "Not Null ") + String.format("IN %s", whitelist);
    }

    public int hashCode() {
        return Objects.hash(nullable, whitelist);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        WhitelistFieldSpec other = (WhitelistFieldSpec) obj;
        return Objects.equals(nullable, other.nullable)
            && Objects.equals(whitelist, other.whitelist);
    }
}
