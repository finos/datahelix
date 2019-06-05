package com.scottlogic.deg.profile.v0_1;

import java.util.Arrays;

public enum AtomicConstraintType {

    IS_EQUAL_TO_CONSTANT("equalTo"),
    IS_IN_SET("inSet"),
    IS_NULL("null"),
    IS_OF_TYPE("ofType"),

    MATCHES_REGEX("matchingRegex"),
    CONTAINS_REGEX("containingRegex"),
    FORMATTED_AS("formattedAs"),
    A_VALID("aValid"),
    A_TYPICAL("aTypical"),

    // String
    HAS_LENGTH("ofLength"),
    IS_STRING_LONGER_THAN("longerThan"),
    IS_STRING_SHORTER_THAN("shorterThan"),

    // Numeric
    IS_GREATER_THAN_CONSTANT("greaterThan"),
    IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT("greaterThanOrEqualTo"),
    IS_LESS_THAN_CONSTANT("lessThan"),
    IS_LESS_THAN_OR_EQUAL_TO_CONSTANT("lessThanOrEqualTo"),

    // DateTime
    IS_AFTER_CONSTANT_DATE_TIME("after"),
    IS_AFTER_OR_EQUAL_TO_CONSTANT_DATE_TIME("afterOrAt"),
    IS_BEFORE_CONSTANT_DATE_TIME("before"),
    IS_BEFORE_OR_EQUAL_TO_CONSTANT_DATE_TIME("beforeOrAt"),

    IS_GRANULAR_TO("granularTo");

    private final String text;

    AtomicConstraintType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getText() { return text; }

    public static AtomicConstraintType fromText(String text){
        return Arrays.stream(AtomicConstraintType.values())
            .filter(x->x.toString().equalsIgnoreCase(text))
            .findFirst().orElse(null);
    }
}
