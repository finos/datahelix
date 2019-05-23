package com.scottlogic.deg.generator.guice;

import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.v0_1.AtomicConstraintType;

public class AtomicConstraintTypeMapper {
    public Class toConstraintClass(AtomicConstraintType type) {
        switch (type) {
            case IS_NULL:
                return IsNullConstraint.class;
            case IS_OF_TYPE:
                return IsOfTypeConstraint.class;
            case MATCHES_REGEX:
                return MatchesRegexConstraint.class;
            case CONTAINS_REGEX:
                return ContainsRegexConstraint.class;
            case FORMATTED_AS:
                return FormatConstraint.class;
            case A_VALID:
                return MatchesStandardConstraint.class;
            case HAS_LENGTH:
                return StringHasLengthConstraint.class;
            case IS_STRING_LONGER_THAN:
                return IsStringLongerThanConstraint.class;
            case IS_STRING_SHORTER_THAN:
                return IsStringShorterThanConstraint.class;
            case IS_GREATER_THAN_CONSTANT:
                return IsGreaterThanConstantConstraint.class;
            case IS_GREATER_THAN_OR_EQUAL_TO_CONSTANT:
                return IsGreaterThanOrEqualToConstantConstraint.class;
            case IS_LESS_THAN_CONSTANT:
                return IsLessThanConstantConstraint.class;
            case IS_LESS_THAN_OR_EQUAL_TO_CONSTANT:
                return IsLessThanOrEqualToConstantConstraint.class;
            case IS_AFTER_CONSTANT_DATE_TIME:
                return IsAfterConstantDateTimeConstraint.class;
            case IS_AFTER_OR_EQUAL_TO_CONSTANT_DATE_TIME:
                return IsAfterOrEqualToConstantDateTimeConstraint.class;
            case IS_BEFORE_CONSTANT_DATE_TIME:
                return IsBeforeConstantDateTimeConstraint.class;
            case IS_BEFORE_OR_EQUAL_TO_CONSTANT_DATE_TIME:
                return IsBeforeOrEqualToConstantDateTimeConstraint.class;
            case IS_GRANULAR_TO:
                return IsGranularToNumericConstraint.class;
            case IS_EQUAL_TO_CONSTANT:
            case IS_IN_SET:
                return IsInSetConstraint.class;
            default:
                throw new UnsupportedOperationException();
        }
    }
}

