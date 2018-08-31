package com.scottlogic.deg.schemas.v3;

public enum AtomicConstraintType {

    ISEQUALTOCONSTANT("equalTo"),
    ISINSET("inSet"),
    ISNULL("null"),
    ISOFTYPE("ofType"),

    MATCHESREGEX("matchingRegex"),
    FORMATTEDAS("formattedAs"),
    AVALID("aValid"),

    // String
    HASLENGTH("hasLength"),
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
    ISBEFOREOREQUALTOCONSTANTDATETIME("beforeOrAt");

    private final String text;

    AtomicConstraintType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
