package com.scottlogic.deg.profile.common;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ConstraintType {
    EQUAL_TO(ConstraintTypeJsonProperty.EQUAL_TO),
    EQUAL_TO_FIELD(ConstraintTypeJsonProperty.EQUAL_TO_FIELD),
    IN_SET(ConstraintTypeJsonProperty.IN_SET),
    IN_MAP(ConstraintTypeJsonProperty.IN_MAP),
    IS_NULL(ConstraintTypeJsonProperty.IS_NULL),
    GRANULAR_TO(ConstraintTypeJsonProperty.GRANULAR_TO),
    MATCHES_REGEX(ConstraintTypeJsonProperty.MATCHES_REGEX),
    CONTAINS_REGEX(ConstraintTypeJsonProperty.CONTAINS_REGEX),
    OF_LENGTH(ConstraintTypeJsonProperty.OF_LENGTH),
    LONGER_THAN(ConstraintTypeJsonProperty.LONGER_THAN),
    SHORTER_THAN(ConstraintTypeJsonProperty.SHORTER_THAN),
    GREATER_THAN(ConstraintTypeJsonProperty.GREATER_THAN),
    GREATER_THAN_FIELD(ConstraintTypeJsonProperty.GREATER_THAN_FIELD),
    GREATER_THAN_OR_EQUAL_TO(ConstraintTypeJsonProperty.GREATER_THAN_OR_EQUAL_TO),
    GREATER_THAN_OR_EQUAL_TO_FIELD(ConstraintTypeJsonProperty.GREATER_THAN_OR_EQUAL_TO_FIELD),
    LESS_THAN(ConstraintTypeJsonProperty.LESS_THAN),
    LESS_THAN_FIELD(ConstraintTypeJsonProperty.LESS_THAN_FIELD),
    LESS_THAN_OR_EQUAL_TO(ConstraintTypeJsonProperty.LESS_THAN_OR_EQUAL_TO),
    LESS_THAN_OR_EQUAL_TO_FIELD(ConstraintTypeJsonProperty.LESS_THAN_OR_EQUAL_TO_FIELD),
    AFTER(ConstraintTypeJsonProperty.AFTER),
    AFTER_FIELD(ConstraintTypeJsonProperty.AFTER_FIELD),
    AFTER_OR_AT(ConstraintTypeJsonProperty.AFTER_OR_AT),
    AFTER_OR_AT_FIELD(ConstraintTypeJsonProperty.AFTER_OR_AT_FIELD),
    BEFORE(ConstraintTypeJsonProperty.BEFORE),
    BEFORE_FIELD(ConstraintTypeJsonProperty.BEFORE_FIELD),
    BEFORE_OR_AT(ConstraintTypeJsonProperty.BEFORE_OR_AT),
    BEFORE_OR_AT_FIELD(ConstraintTypeJsonProperty.BEFORE_OR_AT_FIELD),
    NOT(ConstraintTypeJsonProperty.NOT),
    ANY_OF(ConstraintTypeJsonProperty.ANY_OF),
    ALL_OF(ConstraintTypeJsonProperty.ALL_OF),
    IF(ConstraintTypeJsonProperty.IF);

    @JsonValue
    public final String propertyName;

    ConstraintType(String propertyName) {
        this.propertyName = propertyName;
    }

    public static ConstraintType fromPropertyName(String propertyName) {
        return Arrays.stream(values()).collect(Collectors.toMap(o -> o.propertyName, Function.identity())).get(propertyName);
    }
}

