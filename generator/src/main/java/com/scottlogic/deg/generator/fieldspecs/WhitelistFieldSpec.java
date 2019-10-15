package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.generation.fieldvaluesources.CannedValuesFieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;

import java.util.Objects;
import java.util.Set;

public class WhitelistFieldSpec extends BaseFieldSpec {

    private final DistributedList<Object> whitelist;

    public WhitelistFieldSpec(DistributedList<Object> whitelist, boolean nullable) {
        super(nullable);
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
