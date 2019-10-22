package com.scottlogic.deg.profile.reader.validation;

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.profile.DateTimeGranularity;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.profile.NumericGranularity;
import com.scottlogic.deg.common.util.Defaults;
import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.profile.common.ConstraintType;
import com.scottlogic.deg.profile.reader.InvalidProfileException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.scottlogic.deg.common.profile.FieldType.*;
import static com.scottlogic.deg.profile.common.ConstraintType.GRANULAR_TO;
import static com.scottlogic.deg.profile.common.ConstraintType.IS_NULL;

public class ConstraintTypeValidator {

    public static void validateTypeIs(Field field, ConstraintType type, FieldType s) {
        if (field.getType() == null){
            throw new ValidationException("Field [" + field.name + "]: is not typed; add its type to the field definition");
        }
        if (field.getType() != s){
            throw new ValidationException("Field [" + field.name + "]: is type " + field.getType() + " , but you are trying to apply a " + type.propertyName + " constraint which requires " + s);
        }
    }

    public static void validateAny(Field field, ConstraintType type, Object value) {
        if (value instanceof OffsetDateTime) {
            validateTypeIs(field, type, DATETIME);
        }
        else if (value instanceof Number){
            validateTypeIs(field, type, NUMERIC);
        }
        else {
            validateTypeIs(field, type, STRING);
        }
    }

    public static void validateSet(Field field, ConstraintType type, DistributedList distributedList) {
        distributedList.stream()
            .filter(Objects::nonNull)
            .forEach(val->validateAny(field, type, val));
    }

    private static void validateNumber(ConstraintType type, Object value) {
        if (!(value instanceof Number)){
            throw new ValidationException(
                String.format("Couldn't recognise 'value' property, it must be an Number but was a %s with value `%s`",
                    value.getClass().getSimpleName(), value));
        }

        ensureValueBetween(type, (Number) value, Defaults.NUMERIC_MIN, Defaults.NUMERIC_MAX);

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

    private static void ensureValueBetween(ConstraintType type, Number value, BigDecimal min, BigDecimal max) {
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