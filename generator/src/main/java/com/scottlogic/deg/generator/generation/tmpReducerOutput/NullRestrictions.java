package com.scottlogic.deg.generator.generation.tmpReducerOutput;

public class NullRestrictions {
    public Nullness nullness;

    public enum Nullness {
        MustBeNull,
        MustNotBeNull
    }
}
