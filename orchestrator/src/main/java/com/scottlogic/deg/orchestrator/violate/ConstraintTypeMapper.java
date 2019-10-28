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

import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.common.ConstraintType;

public class ConstraintTypeMapper
{
    public Class toConstraintClass(ConstraintType type) {
        switch (type) {
            case IS_NULL:
                return IsNullConstraint.class;
            case MATCHES_REGEX:
                return MatchesRegexConstraint.class;
            case CONTAINS_REGEX:
                return ContainsRegexConstraint.class;
            case OF_LENGTH:
                return StringHasLengthConstraint.class;
            case LONGER_THAN:
                return IsStringLongerThanConstraint.class;
            case SHORTER_THAN:
                return IsStringShorterThanConstraint.class;
            case GREATER_THAN:
                return IsGreaterThanConstantConstraint.class;
            case GREATER_THAN_OR_EQUAL_TO:
                return IsGreaterThanOrEqualToConstantConstraint.class;
            case LESS_THAN:
                return IsLessThanConstantConstraint.class;
            case LESS_THAN_OR_EQUAL_TO:
                return IsLessThanOrEqualToConstantConstraint.class;
            case AFTER:
                return IsAfterConstantDateTimeConstraint.class;
            case AFTER_OR_AT:
                return IsAfterOrEqualToConstantDateTimeConstraint.class;
            case BEFORE:
                return IsBeforeConstantDateTimeConstraint.class;
            case BEFORE_OR_AT:
                return IsBeforeOrEqualToConstantDateTimeConstraint.class;
            case GRANULAR_TO:
                return IsGranularToNumericConstraint.class;
            case EQUAL_TO:
                return EqualToConstraint.class;
            case IN_SET:
                return IsInSetConstraint.class;
            default:
                throw new UnsupportedOperationException();
        }
    }
}

