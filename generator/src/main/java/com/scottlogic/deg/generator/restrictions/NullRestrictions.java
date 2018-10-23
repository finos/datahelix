package com.scottlogic.deg.generator.restrictions;

public class NullRestrictions {
    public Nullness nullness;

    public enum Nullness {
        MustBeNull,
        MustNotBeNull
    }

    public String toString() {
        switch (nullness){
            case MustBeNull:
                return "null";
            case MustNotBeNull:
                return "NOT null";
        }

        return nullness.toString();
    }
}
