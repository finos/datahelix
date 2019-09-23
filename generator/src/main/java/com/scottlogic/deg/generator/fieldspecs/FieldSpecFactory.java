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

package com.scottlogic.deg.generator.fieldspecs;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyDistributedSet;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.common.util.NumberUtils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.regex.Pattern;

import static com.scottlogic.deg.common.profile.constraints.atomic.StandardConstraintTypes.RIC;

public class FieldSpecFactory {
    private final StringRestrictionsFactory stringRestrictionsFactory;

    @Inject
    public FieldSpecFactory(StringRestrictionsFactory stringRestrictionsFactory) {
        this.stringRestrictionsFactory = stringRestrictionsFactory;
    }

    public FieldSpec construct(Field field, AtomicConstraint constraint) {
        return construct(field, constraint, false);
    }

    private FieldSpec construct(Field field, AtomicConstraint constraint, boolean negate) {
        if (constraint instanceof ViolatedAtomicConstraint) {
            return construct(field, ((ViolatedAtomicConstraint) constraint).violatedConstraint, negate);
        } else if (constraint instanceof NotConstraint) {
            return construct(field, ((NotConstraint) constraint).negatedConstraint, !negate);
        } else if (constraint instanceof IsInSetConstraint) {
            return construct(field, (IsInSetConstraint) constraint, negate);
        } else if (constraint instanceof EqualToConstraint) {
            return construct(field, (EqualToConstraint) constraint, negate);
        } else if (constraint instanceof IsGreaterThanConstantConstraint) {
            return construct(field, (IsGreaterThanConstantConstraint) constraint, negate);
        } else if (constraint instanceof IsGreaterThanOrEqualToConstantConstraint) {
            return construct(field, (IsGreaterThanOrEqualToConstantConstraint) constraint, negate);
        } else if (constraint instanceof IsLessThanConstantConstraint) {
            return construct(field, (IsLessThanConstantConstraint) constraint, negate);
        } else if (constraint instanceof IsLessThanOrEqualToConstantConstraint) {
            return construct(field, (IsLessThanOrEqualToConstantConstraint) constraint, negate);
        } else if (constraint instanceof IsAfterConstantDateTimeConstraint) {
            return construct(field, (IsAfterConstantDateTimeConstraint) constraint, negate);
        } else if (constraint instanceof IsAfterOrEqualToConstantDateTimeConstraint) {
            return construct(field, (IsAfterOrEqualToConstantDateTimeConstraint) constraint, negate);
        } else if (constraint instanceof IsBeforeConstantDateTimeConstraint) {
            return construct(field, (IsBeforeConstantDateTimeConstraint) constraint, negate);
        } else if (constraint instanceof IsBeforeOrEqualToConstantDateTimeConstraint) {
            return construct(field, (IsBeforeOrEqualToConstantDateTimeConstraint) constraint, negate);
        } else if (constraint instanceof IsGranularToNumericConstraint) {
            return construct(field, (IsGranularToNumericConstraint) constraint, negate);
        } else if (constraint instanceof IsGranularToDateConstraint) {
            return construct(field, (IsGranularToDateConstraint) constraint, negate);
        } else if (constraint instanceof IsNullConstraint) {
            return constructIsNull(field, negate);
        } else if (constraint instanceof MatchesRegexConstraint) {
            return construct(field, (MatchesRegexConstraint) constraint, negate);
        } else if (constraint instanceof ContainsRegexConstraint) {
            return construct(field, (ContainsRegexConstraint) constraint, negate);
        } else if (constraint instanceof MatchesStandardConstraint) {
            return construct(field, (MatchesStandardConstraint) constraint, negate);
        } else if (constraint instanceof IsOfTypeConstraint) {
            return construct(field, (IsOfTypeConstraint) constraint, negate);
        } else if (constraint instanceof StringHasLengthConstraint) {
            return construct(field, (StringHasLengthConstraint) constraint, negate);
        } else if (constraint instanceof IsStringLongerThanConstraint) {
            return construct(field, (IsStringLongerThanConstraint) constraint, negate);
        } else if (constraint instanceof IsStringShorterThanConstraint) {
            return construct(field, (IsStringShorterThanConstraint) constraint, negate);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private FieldSpec construct(Field field, IsInSetConstraint constraint, boolean negate) {
        if (negate) {
            return FieldSpec.fromType(field.getType()).withBlacklist(constraint.legalValuesWithoutFrequency());
        }

        return FieldSpec.fromType(field.getType()).withWhitelist(constraint.legalValues);
    }

    private FieldSpec construct(Field field, EqualToConstraint constraint, boolean negate) {
        if (negate) {
            return FieldSpec.fromType(field.getType()).withBlacklist(Collections.singleton(constraint.value));
        }

        return FieldSpec.fromType(field.getType())
            .withWhitelist(FrequencyDistributedSet.singleton(constraint.value))
            .withNotNull();
    }

    private FieldSpec constructIsNull(Field field, boolean negate) {
        if (negate) {
            return FieldSpec.fromType(field.getType()).withNotNull();
        }

        return FieldSpec.NullOnly;
    }

    private FieldSpec construct(Field field, IsOfTypeConstraint constraint, boolean negate) {
        if (negate) {
            throw new UnsupportedOperationException("cannot negate types");
        }

        return FieldSpec.fromType(field.getType()).withType(constraint.requiredType);
    }

    private FieldSpec construct(Field field, IsGreaterThanConstantConstraint constraint, boolean negate) {
        return constructGreaterThanConstraint(field, constraint.referenceValue, false, negate);
    }

    private FieldSpec construct(Field field, IsGreaterThanOrEqualToConstantConstraint constraint, boolean negate) {
        return constructGreaterThanConstraint(field, constraint.referenceValue, true, negate);
    }

    private FieldSpec constructGreaterThanConstraint(Field field, Number limitValue, boolean inclusive, boolean negate) {
        final NumericRestrictions numericRestrictions = new NumericRestrictions();

        final BigDecimal limit = NumberUtils.coerceToBigDecimal(limitValue);
        if (negate) {
            numericRestrictions.max = new NumericLimit<>(
                limit,
                !inclusive);
        } else {
            numericRestrictions.min = new NumericLimit<>(
                limit,
                inclusive);
        }

        return FieldSpec.fromType(field.getType()).withNumericRestrictions(numericRestrictions);
    }

    private FieldSpec construct(Field field, IsLessThanConstantConstraint constraint, boolean negate) {
        return constructLessThanConstraint(field, constraint.referenceValue, false, negate);
    }

    private FieldSpec construct(Field field, IsLessThanOrEqualToConstantConstraint constraint, boolean negate) {
        return constructLessThanConstraint(field, constraint.referenceValue, true, negate);
    }

    private FieldSpec constructLessThanConstraint(Field field, Number limitValue, boolean inclusive, boolean negate) {
        final NumericRestrictions numericRestrictions = new NumericRestrictions();
        final BigDecimal limit = NumberUtils.coerceToBigDecimal(limitValue);
        if (negate) {
            numericRestrictions.min = new NumericLimit<>(
                limit,
                !inclusive);
        } else {
            numericRestrictions.max = new NumericLimit<>(
                limit,
                inclusive);
        }

        return FieldSpec.fromType(field.getType()).withNumericRestrictions(numericRestrictions);
    }

    private FieldSpec construct(Field field, IsGranularToNumericConstraint constraint, boolean negate) {
        if (negate) {
            // negated granularity is a future enhancement
            return FieldSpec.fromType(field.getType());
        }

        return FieldSpec.fromType(field.getType()).withNumericRestrictions(new NumericRestrictions(constraint.granularity.getNumericGranularity().scale()));
    }

    private FieldSpec construct(Field field, IsGranularToDateConstraint constraint, boolean negate) {
        if (negate) {
            // negated granularity is a future enhancement
            return FieldSpec.fromType(field.getType());
        }

        return FieldSpec.fromType(field.getType()).withDateTimeRestrictions(new DateTimeRestrictions(constraint.granularity.getGranularity()));
    }

    private FieldSpec construct(Field field, IsAfterConstantDateTimeConstraint constraint, boolean negate) {
        return constructIsAfterConstraint(field, constraint.referenceValue, false, negate);
    }

    private FieldSpec construct(Field field, IsAfterOrEqualToConstantDateTimeConstraint constraint, boolean negate) {
        return constructIsAfterConstraint(field, constraint.referenceValue, true, negate);
    }

    private FieldSpec constructIsAfterConstraint(Field field, OffsetDateTime limit, boolean inclusive, boolean negate) {
        final DateTimeRestrictions dateTimeRestrictions = new DateTimeRestrictions();

        if (negate) {
            dateTimeRestrictions.max = new DateTimeLimit(limit, !inclusive);
        } else {
            dateTimeRestrictions.min = new DateTimeLimit(limit, inclusive);
        }

        return FieldSpec.fromType(field.getType()).withDateTimeRestrictions(dateTimeRestrictions);
    }

    private FieldSpec construct(Field field, IsBeforeConstantDateTimeConstraint constraint, boolean negate) {
        return constructIsBeforeConstraint(field, constraint.referenceValue, false, negate);
    }

    private FieldSpec construct(Field field, IsBeforeOrEqualToConstantDateTimeConstraint constraint, boolean negate) {
        return constructIsBeforeConstraint(field, constraint.referenceValue, true, negate);
    }

    private FieldSpec constructIsBeforeConstraint(Field field, OffsetDateTime limit, boolean inclusive, boolean negate) {
        final DateTimeRestrictions dateTimeRestrictions = new DateTimeRestrictions();

        if (negate) {
            dateTimeRestrictions.min = new DateTimeLimit(limit, !inclusive);
        } else {
            dateTimeRestrictions.max = new DateTimeLimit(limit, inclusive);
        }

        return FieldSpec.fromType(field.getType()).withDateTimeRestrictions(dateTimeRestrictions);
    }

    private FieldSpec construct(Field field, MatchesRegexConstraint constraint, boolean negate) {
        return FieldSpec.fromType(field.getType())
            .withStringRestrictions(stringRestrictionsFactory.forStringMatching(constraint.regex, negate));
    }

    private FieldSpec construct(Field field, ContainsRegexConstraint constraint, boolean negate) {
        return FieldSpec.fromType(field.getType())
            .withStringRestrictions(stringRestrictionsFactory.forStringContaining(constraint.regex, negate));
    }

    private FieldSpec construct(Field field, MatchesStandardConstraint constraint, boolean negate) {
        if (constraint.standard.equals(RIC)) {
            return construct(field, new MatchesRegexConstraint(constraint.field, Pattern.compile(RIC.getRegex())), negate);
        }

        if (negate){
            return construct(field, new MatchesRegexConstraint(constraint.field, Pattern.compile(constraint.standard.getRegex())), negate);
        }

        return FieldSpec.fromType(field.getType())
            .withStringRestrictions(new MatchesStandardStringRestrictions(constraint.standard));
    }

    private FieldSpec construct(Field field, StringHasLengthConstraint constraint, boolean negate) {
        return FieldSpec.fromType(field.getType())
            .withStringRestrictions(stringRestrictionsFactory.forLength(constraint.referenceValue, negate));
    }

    private FieldSpec construct(Field field, IsStringShorterThanConstraint constraint, boolean negate) {
        return FieldSpec.fromType(field.getType())
            .withStringRestrictions(
                negate
                    ? stringRestrictionsFactory.forMinLength(constraint.referenceValue)
                    : stringRestrictionsFactory.forMaxLength(constraint.referenceValue - 1)
            );
    }

    private FieldSpec construct(Field field, IsStringLongerThanConstraint constraint, boolean negate) {
        return FieldSpec.fromType(field.getType())
            .withStringRestrictions(
                negate
                    ? stringRestrictionsFactory.forMaxLength(constraint.referenceValue)
                    : stringRestrictionsFactory.forMinLength(constraint.referenceValue + 1)
            );
    }

}
