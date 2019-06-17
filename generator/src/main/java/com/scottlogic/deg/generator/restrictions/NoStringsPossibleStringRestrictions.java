package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.generation.string.NoStringsStringGenerator;
import com.scottlogic.deg.generator.generation.string.StringGenerator;

/**
 * Represents a set of string restrictions that could never produce and values
 */
public class NoStringsPossibleStringRestrictions implements StringRestrictions {
    private final String reason;

    NoStringsPossibleStringRestrictions(String reason) {
        this.reason = reason;
    }

    @Override
    public MergeResult<StringRestrictions> intersect(StringRestrictions other) {
        return new MergeResult<>(this);
    }

    public String toString(){
        return String.format("No strings can be generated: %s", reason);
    }

    @Override
    public boolean match(String x) {
        return false;
    }

    @Override
    public StringGenerator createGenerator() {
        return new NoStringsStringGenerator(reason);
    }
}
