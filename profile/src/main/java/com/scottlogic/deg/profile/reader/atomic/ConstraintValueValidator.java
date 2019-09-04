package com.scottlogic.deg.profile.reader.atomic;

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraintdetail.ParsedDateGranularity;
import com.scottlogic.deg.common.profile.constraintdetail.ParsedGranularity;
import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.dto.AtomicConstraintType;
import com.scottlogic.deg.profile.reader.InvalidProfileException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import static com.scottlogic.deg.profile.dto.AtomicConstraintType.IS_NULL;

public class ConstraintValueValidator {

    public static void validate(String field, AtomicConstraintType type, Object value){
        try {
            validateConstraintValue(type, value);
        } catch (IllegalArgumentException | ValidationException e){
            throw new InvalidProfileException(String.format("Field [%s]: %s", field, e.getMessage()));
        }
    }

    private static void validateConstraintValue(AtomicConstraintType type, Object value){

        if (type == IS_NULL){
            validateNull(value);
            return;
        }

        validateNotNull(value);

        switch (type) {
            case IS_EQUAL_TO_CONSTANT:
                validateAny(value);
                break;
            case IS_IN_SET:
                validateSet(value);
                break;
            case IS_OF_TYPE:
                validateTypes(value);
                break;

            case MATCHES_REGEX:
            case CONTAINS_REGEX:
                validatePattern(value);
                break;

            case HAS_LENGTH:
            case IS_STRING_SHORTER_THAN:
            case IS_STRING_LONGER_THAN:
                validateInteger(value);
                break;

            case IS_GREATER_THAN_CONSTANT:
            case IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT:
            case IS_LESS_THAN_CONSTANT:
            case IS_LESS_THAN_OR_EQUAL_TO_CONSTANT:
                validateNumber(value);
                break;

            case IS_AFTER_CONSTANT_DATE_TIME:
            case IS_AFTER_OR_EQUAL_TO_CONSTANT_DATE_TIME:
            case IS_BEFORE_CONSTANT_DATE_TIME:
            case IS_BEFORE_OR_EQUAL_TO_CONSTANT_DATE_TIME:
                validateDateTime(value);
                break;

            case IS_GRANULAR_TO:
                validateGranularity(value);
        }
    }

    private static void validateNotNull(Object value) {

    }


    private static void validateAny(Object value) {
        if (value instanceof Number){
            validateNumber(value);
        }
    }

    private static void validateSet(Object value) {
    }

    private static void validateNull(Object value) {
    }

    private static void validateTypes(Object value) {
    }

    private static void validatePattern(Object value) {
    }

    private static void validateInteger(Object value) {
    }

    private static void validateNumber(Object value) {
    }

    private static void validateDateTime(Object value) {
    }

    private static void validateGranularity(Object value) {
        if (value instanceof Number) {
            ParsedGranularity.parse(value);
        }
        else if (value instanceof String) {
            ParsedDateGranularity.parse((String) value);
        }
        else {
            throw new ValidationException("Couldn't recognise granularity value, it must be either a negative power of ten or one of the supported datetime units.");
        }
    }
}
