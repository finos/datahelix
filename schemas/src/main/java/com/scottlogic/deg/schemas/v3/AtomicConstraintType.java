package com.scottlogic.deg.schemas.v3;

public enum AtomicConstraintType {

    ISEQUALTOCONSTANT("equalTo"),
    ISINSET("inSet"),
    ISNULL("null"),
    ISOFTYPE("ofType"),
    NOT("not"),

    MATCHESREGEX("matchingRegex"),
    FORMATTEDAS("formattedAs"),

    // String
    HASLENGTH("hasLength"),
    ISSTRINGLONGERTHAN("longerThan"),
    ISSTRINGSHORTERTHAN("shorterThan"),

    // Numeric
    ISGREATERTHANCONSTANT("greaterThan"),
    ISGREATERTHANOREQUALTOCONSTANT("greaterThanOrEqualTo"),
    ISLESSTHANCONSTANT("lessThanConstant"),
    ISLESSTHANOREQUALTOCONSTANT("lessThanOrEqualToConstant"),

    // Temporal
    ISAFTERCONSTANTDATETIME("afterConstantDateTime"),
    ISAFTEROREQUALTOCONSTANTDATETIME("afterOrEqualToConstantDateTime"),
    ISBEFORECONSTANTDATETIME("beforeConstantDateTime"),
    ISBEFOREOREQUALTOCONSTANTDATETIME("beforeOrEqualToConstantDateTime");

    private final String text;

    AtomicConstraintType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
