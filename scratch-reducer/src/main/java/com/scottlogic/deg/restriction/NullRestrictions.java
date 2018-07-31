package com.scottlogic.deg.restriction;

import java.util.HashSet;
import java.util.Set;

public class NullRestrictions {
    public Nullness nullness;

    public enum Nullness {
        MustBeNull,
        MustNotBeNull
    }
}
