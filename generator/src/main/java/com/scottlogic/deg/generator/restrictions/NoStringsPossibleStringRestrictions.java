package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.generation.NoStringsStringGenerator;
import com.scottlogic.deg.generator.generation.StringGenerator;

public class NoStringsPossibleStringRestrictions implements StringRestrictions {
    private final String reason;

    public NoStringsPossibleStringRestrictions(String reason) {
        this.reason = reason;
    }

    @Override
    public StringRestrictions intersect(StringRestrictions other) {
        return this;
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
