package com.scottlogic.deg.profile.reader.atomic;

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraintdetail.ParsedDateGranularity;
import com.scottlogic.deg.common.profile.constraintdetail.ParsedGranularity;
import com.scottlogic.deg.common.util.Defaults;
import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedSet;
import com.scottlogic.deg.profile.dto.AtomicConstraintType;
import com.scottlogic.deg.profile.reader.InvalidProfileException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

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
                validateAny(type, value);
                break;
            case IS_IN_SET:
                validateSet(type, value);
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
                validateStringLengthInt(type, value);
                break;

            case IS_GREATER_THAN_CONSTANT:
            case IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT:
            case IS_LESS_THAN_CONSTANT:
            case IS_LESS_THAN_OR_EQUAL_TO_CONSTANT:
                validateNumber(type, value);
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

    private static void validateNull(Object value) {
    }

    private static void validateNotNull(Object value) {
        if (value == null){
            throw new ValidationException("Couldn't recognise 'value' property, it must be set to a value");
        }
    }

    private static void validateAny(AtomicConstraintType type, Object value) {
        if (value instanceof OffsetDateTime) {
            validateDateTime(value);
        }
        else if (value instanceof Number){
            validateNumber(type, value);
        }
    }

    private static void validateSet(AtomicConstraintType type, Object value) {
        if (!(value instanceof DistributedSet)){
            throw new ValidationException("Couldn't recognise 'values' property, it must not contain 'null'");
        }

        ((DistributedSet) value).set().forEach(val->validateAny(type, val));
    }

    private static void validateTypes(Object value) {
        OfTypeConstraintFactory.create(new Field("validation"), (String)value);
    }

    private static void validatePattern(Object value) {
    }

    private static void validateStringLengthInt(AtomicConstraintType type, Object value) {
        if (!(value instanceof Number)){
            throw new ValidationException(
                String.format("Couldn't recognise 'value' property, it must be an Integer but was a %s with value `%s`",
                    value.getClass().getSimpleName(), value));
        }

        BigDecimal valueAsBigDecimal = NumberUtils.coerceToBigDecimal(value);
        if (valueAsBigDecimal.stripTrailingZeros().scale() > 0) {
            throw new ValidationException(String.format(
                "Couldn't recognise 'value' property, it must be an integer but was a decimal with value `%s`",
                value
            ));
        }
        ensureValueBetween(type, value, BigDecimal.ZERO, BigDecimal.valueOf(Defaults.MAX_STRING_LENGTH));
    }

    private static void validateNumber(AtomicConstraintType type, Object value) {
        if (!(value instanceof Number)){
            throw new ValidationException(
                String.format("Couldn't recognise 'value' property, it must be an Number but was a %s with value `%s`",
                    value.getClass().getSimpleName(), value));
        }

        ensureValueBetween(type, value, Defaults.NUMERIC_MIN, Defaults.NUMERIC_MAX);

    }

    private static void validateDateTime(Object value) {
        if (!(value instanceof OffsetDateTime)){
            throw new ValidationException(String.format("Dates should be expressed in object format e.g. { \"date\": \"yyyy-MM-ddTHH:mm:ss.SSS[Z]\" }", value));
        }
        OffsetDateTime offsetDateTime = (OffsetDateTime) value;

        if (offsetDateTime != null && (offsetDateTime.getYear() > 9999 || offsetDateTime.getYear() < 1)) {
            throw new ValidationException("Dates must be between 0001-01-01T00:00:00.000Z and 9999-12-31T23:59:59.999Z");
        }
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

    private static void ensureValueBetween(AtomicConstraintType type, Object value, BigDecimal min, BigDecimal max) {
        BigDecimal valueAsBigDecimal = NumberUtils.coerceToBigDecimal(value);
        if (valueAsBigDecimal.compareTo(min) < 0) {
            throw new InvalidProfileException(String.format(
                "%s constraint must have an operand/value >= %s, currently is %s",
                type, min.toPlainString(), valueAsBigDecimal.toPlainString()));
        }

        if (valueAsBigDecimal.compareTo(max) > 0) {
            throw new InvalidProfileException(String.format(
                "%s constraint must have an operand/value <= %s, currently is %s",
                type, max.toPlainString(), valueAsBigDecimal.toPlainString()));
        }
    }
}
