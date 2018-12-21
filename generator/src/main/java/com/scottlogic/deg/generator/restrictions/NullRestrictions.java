package com.scottlogic.deg.generator.restrictions;

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
}
