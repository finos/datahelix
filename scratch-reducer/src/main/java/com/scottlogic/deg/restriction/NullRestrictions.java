package com.scottlogic.deg.restriction;

public class NullRestrictions {
    public Nullness nullness;

    public enum Nullness {
        MustBeNull,
        MustNotBeNull
    }
}
