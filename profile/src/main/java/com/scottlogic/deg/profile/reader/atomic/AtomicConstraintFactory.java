package com.scottlogic.deg.profile.reader.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedSet;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyDistributedSet;
import com.scottlogic.deg.profile.dto.AtomicConstraintType;
import com.scottlogic.deg.profile.reader.ConstraintReaderHelpers;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

public class AtomicConstraintFactory {
    public static AtomicConstraint create(AtomicConstraintType type, Field field, Object value){
        switch (type) {
            case IS_EQUAL_TO_CONSTANT:
                return new EqualToConstraint(field, getType(value));
            case IS_IN_SET:
                return new IsInSetConstraint(field, collection(value));
            case IS_NULL:
                return new IsNullConstraint(field);
            case IS_OF_TYPE:
                throw new UnsupportedOperationException(""); //TODO uhh

            case MATCHES_REGEX:
                return new MatchesRegexConstraint(field, pattern(value));
            case CONTAINS_REGEX:
                return new ContainsRegexConstraint(field, pattern(value));

            case HAS_LENGTH:
                return new StringHasLengthConstraint(field, integer(value));
            case IS_STRING_SHORTER_THAN:
                return new IsStringShorterThanConstraint(field, integer(value));
            case IS_STRING_LONGER_THAN:
                return new IsStringLongerThanConstraint(field, integer(value));

            case IS_GREATER_THAN_CONSTANT:
                return new IsGreaterThanConstantConstraint(field, number(value));
            case IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT:
                return new IsGreaterThanOrEqualToConstantConstraint(field, number(value));
            case IS_LESS_THAN_CONSTANT:
                return new IsLessThanConstantConstraint(field, number(value));
            case IS_LESS_THAN_OR_EQUAL_TO_CONSTANT:
                return new IsLessThanOrEqualToConstantConstraint(field, number(value));

            case IS_AFTER_CONSTANT_DATE_TIME:
                return new IsAfterConstantDateTimeConstraint(field, dateTime(value));
            case IS_AFTER_OR_EQUAL_TO_CONSTANT_DATE_TIME:
                return new IsAfterOrEqualToConstantDateTimeConstraint(field, dateTime(value));
            case IS_BEFORE_CONSTANT_DATE_TIME:
                return new IsBeforeConstantDateTimeConstraint(field, dateTime(value));
            case IS_BEFORE_OR_EQUAL_TO_CONSTANT_DATE_TIME:
                return new IsBeforeOrEqualToConstantDateTimeConstraint(field, dateTime(value));

            case IS_GRANULAR_TO:
                throw new NotImplementedException();

        }

        throw new NotImplementedException();
    }

    private static DistributedSet<Object> collection(Object value) {
        if (value instanceof DistributedSet){
            return (DistributedSet<Object>) value;
        }
        return FrequencyDistributedSet.uniform((Collection)value);
    }

    private static Object getType(Object value) {
        if (value instanceof Map)
            return dateTime(value);

        if (value instanceof Number)
            return number(value);

        return value;
    }

    private static OffsetDateTime dateTime(Object value) {
        if (value instanceof OffsetDateTime)
            return (OffsetDateTime) value;

        return ConstraintReaderHelpers.getValueAsDate(value);
    }

    private static Number number(Object value) {
        return ConstraintReaderHelpers.getValueAsNumber(value);
    }

    private static int integer(Object value) {
        return (int) value;
    }

    private static Pattern pattern(Object value) {
        if (value instanceof Pattern){
            return (Pattern) value;
        }

        return Pattern.compile(ConstraintReaderHelpers.getValueAsString(value));
    }
}
