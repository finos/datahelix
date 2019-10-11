package com.scottlogic.deg.profile.common;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ConstraintType
{
    EQUAL_TO(ConstraintTypeJsonProperty.EQUAL_TO),
    IN_SET(ConstraintTypeJsonProperty.IN_SET),
    IN_MAP(ConstraintTypeJsonProperty.IN_MAP),
    NULL(ConstraintTypeJsonProperty.NULL),
    GRANULAR_TO(ConstraintTypeJsonProperty.GRANULAR_TO),
    MATCHES_REGEX(ConstraintTypeJsonProperty.MATCHES_REGEX),
    CONTAINS_REGEX(ConstraintTypeJsonProperty.CONTAINS_REGEX),
    OF_LENGTH(ConstraintTypeJsonProperty.OF_LENGTH),
    LONGER_THAN(ConstraintTypeJsonProperty.LONGER_THAN),
    SHORTER_THAN(ConstraintTypeJsonProperty.SHORTER_THAN),
    GREATER_THAN(ConstraintTypeJsonProperty.GREATER_THAN),
    GREATER_THAN_OR_EQUAL_TO(ConstraintTypeJsonProperty.GREATER_THAN_OR_EQUAL_TO),
    LESS_THAN(ConstraintTypeJsonProperty.LESS_THAN),
    LESS_THAN_OR_EQUAL_TO(ConstraintTypeJsonProperty.LESS_THAN_OR_EQUAL_TO),
    AFTER(ConstraintTypeJsonProperty.AFTER),
    AFTER_OR_AT(ConstraintTypeJsonProperty.AFTER_OR_AT),
    BEFORE(ConstraintTypeJsonProperty.BEFORE),
    BEFORE_OR_AT(ConstraintTypeJsonProperty.BEFORE_OR_AT),
    NOT(ConstraintTypeJsonProperty.NOT),
    ANY_OF(ConstraintTypeJsonProperty.ANY_OF),
    ALL_OF(ConstraintTypeJsonProperty.ALL_OF),
    IF(ConstraintTypeJsonProperty.IF);

    @JsonValue
    public final String propertyName;

    ConstraintType(String propertyName)
    {
        this.propertyName = propertyName;
    }

    public static ConstraintType fromPropertyName(String propertyName)
    {
        return Arrays.stream(values()).collect(Collectors.toMap(o -> propertyName, Function.identity())).get(propertyName);
    }
}

