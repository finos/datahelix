package com.scottlogic.deg.generator.restrictions;

import java.util.Objects;

public class NullRestrictions {
    public Nullness nullness;


    public NullRestrictions() {
    }

    public NullRestrictions(Nullness nullness) {
        this.nullness = nullness;
    }

    public String toString() {
        switch (nullness){
            case MUST_BE_NULL:
                return "null";
            case MUST_NOT_BE_NULL:
                return "NOT null";
        }

        return nullness.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NullRestrictions that = (NullRestrictions) o;
        return nullness == that.nullness;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nullness);
    }
}
