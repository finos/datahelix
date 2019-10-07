package com.scottlogic.deg.profile.reader.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraintdetail.NumericGranularityFactory;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.common.profile.constraintdetail.AtomicConstraintType;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.reader.RemoveFromTree;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.scottlogic.deg.profile.reader.atomic.ConstraintReaderHelpers.getDateTimeGranularity;

public class AtomicConstraintFactory {
    public static Constraint create(AtomicConstraintType type, Field field, Object value){
        switch (type) {
            case IS_EQUAL_TO_CONSTANT:
                return new EqualToConstraint(field, value);
            case IS_IN_SET:
                return new IsInSetConstraint(field, (DistributedList<Object>)value);
            case IS_NULL:
                return new IsNullConstraint(field);

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
                return new IsGreaterThanConstantConstraint(field, (BigDecimal) value);
            case IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT:
                return new IsGreaterThanOrEqualToConstantConstraint(field, (BigDecimal)value);
            case IS_LESS_THAN_CONSTANT:
                return new IsLessThanConstantConstraint(field, (BigDecimal)value);
            case IS_LESS_THAN_OR_EQUAL_TO_CONSTANT:
                return new IsLessThanOrEqualToConstantConstraint(field, (BigDecimal) value);

            case IS_AFTER_CONSTANT_DATE_TIME:
                return new IsAfterConstantDateTimeConstraint(field, (OffsetDateTime)value);
            case IS_AFTER_OR_EQUAL_TO_CONSTANT_DATE_TIME:
                return new IsAfterOrEqualToConstantDateTimeConstraint(field, (OffsetDateTime)value);
            case IS_BEFORE_CONSTANT_DATE_TIME:
                return new IsBeforeConstantDateTimeConstraint(field, (OffsetDateTime)value);
            case IS_BEFORE_OR_EQUAL_TO_CONSTANT_DATE_TIME:
                return new IsBeforeOrEqualToConstantDateTimeConstraint(field, (OffsetDateTime)value);

            case IS_GRANULAR_TO:
                if (value instanceof Number)
                    return new IsGranularToNumericConstraint(field, NumericGranularityFactory.create(value));
                else
                    return new IsGranularToDateConstraint(field, getDateTimeGranularity((String)value));

            case IS_UNIQUE:
            case FORMATTED_AS:
                return new RemoveFromTree();

            case IS_OF_TYPE: {
                Optional<Constraint> constraint = OfTypeConstraintFactory.create(field, (String) value);
                return constraint.orElseGet(RemoveFromTree::new);
            }

            default:
                throw new IllegalArgumentException("constraint type not found");
        }
    }

    private static int integer(Object value) {
        return NumberUtils.coerceToBigDecimal(value).intValueExact();
    }

    private static Pattern pattern(Object value) {
        if (value instanceof Pattern){
            return (Pattern) value;
        }

        return Pattern.compile((String)value);
    }
}
