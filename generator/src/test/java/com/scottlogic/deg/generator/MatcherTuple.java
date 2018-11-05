package com.scottlogic.deg.generator;

import org.hamcrest.Matcher;

import java.util.Optional;
import java.util.function.Supplier;

public class MatcherTuple{
    public final Matcher matcher;
    public final Supplier getActualFunc;
    public final Optional<String> overrideDescription;

    public MatcherTuple(Matcher matcher, Supplier getActualFunc) {
        this(matcher, getActualFunc, null);
    }

    public MatcherTuple(Matcher matcher, Supplier getActualFunc, String overrideDescription) {
        this.matcher = matcher;
        this.getActualFunc = getActualFunc;
        this.overrideDescription = overrideDescription == null
            ? Optional.empty()
            : Optional.of(overrideDescription);
    }
}
