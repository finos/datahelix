package com.scottlogic.deg.schemas.v0_1;

import java.util.Arrays;

public enum AtomicConstraintType {

    ISEQUALTOCONSTANT("equalTo"),
    ISINSET("inSet"),
    ISNULL("null"),
    ISOFTYPE("ofType"),

    MATCHESREGEX("matchingRegex"),
    CONTAINSREGEX("containingRegex"),
    FORMATTEDAS("formattedAs"),
    AVALID("aValid"),

    // String
    HASLENGTH("ofLength"),
    ISSTRINGLONGERTHAN("longerThan"),
    ISSTRINGSHORTERTHAN("shorterThan"),

    // Numeric
    ISGREATERTHANCONSTANT("greaterThan"),
    ISGREATERTHANOREQUALTOCONSTANT("greaterThanOrEqualTo"),
    ISLESSTHANCONSTANT("lessThan"),
    ISLESSTHANOREQUALTOCONSTANT("lessThanOrEqualTo"),

    // Temporal
    ISAFTERCONSTANTDATETIME("after"),
    ISAFTEROREQUALTOCONSTANTDATETIME("afterOrAt"),
    ISBEFORECONSTANTDATETIME("before"),
    ISBEFOREOREQUALTOCONSTANTDATETIME("beforeOrAt"),

    ISGRANULARTO("granularTo");

    private final String text;

    AtomicConstraintType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static AtomicConstraintType fromText(String text){
        return Arrays.stream(AtomicConstraintType.values())
            .filter(x->x.toString().equalsIgnoreCase(text))
            .findFirst().orElse(null);
    }
}
