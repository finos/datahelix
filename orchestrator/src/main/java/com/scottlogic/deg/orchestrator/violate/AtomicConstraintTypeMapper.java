/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.orchestrator.violate;

import com.scottlogic.deg.common.profile.constraintdetail.AtomicConstraintType;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;

public class AtomicConstraintTypeMapper {
    public Class toConstraintClass(AtomicConstraintType type) {
        switch (type) {
            case IS_NULL:
                return IsNullConstraint.class;
            case MATCHES_REGEX:
                return MatchesRegexConstraint.class;
            case CONTAINS_REGEX:
                return ContainsRegexConstraint.class;
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
                return EqualToConstraint.class;
            case IS_IN_SET:
                return IsInSetConstraint.class;
            default:
                throw new UnsupportedOperationException();
        }
    }
}

