package com.scottlogic.deg.generator.restrictions;

import java.util.Collections;
import java.util.regex.Pattern;

public class StringRestrictionsFactory {
    public StringRestrictions forStringMatching(Pattern pattern, boolean negate) {
        return new TextualRestrictions(
            null,
            null,
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
            null,
            null,
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
            negate ? null : length,
            negate ? null : length,
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
            null,
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet()
        );
    }

    public StringRestrictions forMaxLength(int length){
        return new TextualRestrictions(
            null,
            length,
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet()
        );
    }
}
