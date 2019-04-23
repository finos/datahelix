package com.scottlogic.deg.generator.restrictions;

import java.util.Collections;
import java.util.regex.Pattern;

public class StringRestrictionsFactory {
    private static final Integer defaultMinLength = 0;
    private static final Integer defaultMaxLength = 255;

    public StringRestrictions forStringMatching(Pattern pattern, boolean negate) {
        return new TextualRestrictions(
            defaultMinLength,
            defaultMaxLength,
            negate
                ? Collections.emptySet()
                : Collections.singleton(pattern),
            Collections.emptySet(),
            Collections.emptySet(),
            negate
                ? Collections.singleton(pattern)
                : Collections.emptySet(),
            Collections.emptySet()
        );
    }

    public StringRestrictions forStringContaining(Pattern pattern, boolean negate) {
        return new TextualRestrictions(
            defaultMinLength,
            defaultMaxLength,
            Collections.emptySet(),
            negate
                ? Collections.emptySet()
                : Collections.singleton(pattern),
            Collections.emptySet(),
            Collections.emptySet(),
            negate
                ? Collections.singleton(pattern)
                : Collections.emptySet()
        );
    }

    public StringRestrictions forLength(int length, boolean negate) {
        return new TextualRestrictions(
            negate ? defaultMinLength : length,
            negate ? defaultMaxLength : length,
            Collections.emptySet(),
            Collections.emptySet(),
            negate
                ? Collections.singleton(length)
                : Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet()
        );
    }

    public StringRestrictions forMinLength(int length){
        return new TextualRestrictions(
            length,
            defaultMaxLength,
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet()
        );
    }

    public StringRestrictions forMaxLength(int length){
        return new TextualRestrictions(
            defaultMinLength,
            length,
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet()
        );
    }
}
