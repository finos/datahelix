package com.scottlogic.deg.generator;

import org.hamcrest.Matcher;

import java.util.function.Supplier;

public class MatcherTuple{
    public final Matcher matcher;
    public final Supplier getActualFunc;

    public MatcherTuple(Matcher matcher, Supplier getActualFunc) {
        this.matcher = matcher;
        this.getActualFunc = getActualFunc;
    }
}
