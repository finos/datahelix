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

    public FieldSpec construct(AtomicConstraint constraint) {
        return construct(constraint, false);
    }

    private FieldSpec construct(AtomicConstraint constraint, boolean negate) {
        if (constraint instanceof ViolatedAtomicConstraint) {
            return construct(((ViolatedAtomicConstraint) constraint).violatedConstraint, negate);
        } else if (constraint instanceof NotConstraint) {
            return construct(((NotConstraint) constraint).negatedConstraint, !negate);
        } else if (constraint instanceof IsInSetConstraint) {
            return construct((IsInSetConstraint) constraint, negate);
        } else if (constraint instanceof EqualToConstraint) {
            return construct((EqualToConstraint) constraint, negate);
        } else if (constraint instanceof IsGreaterThanConstantConstraint) {
            return construct((IsGreaterThanConstantConstraint) constraint, negate);
        } else if (constraint instanceof IsGreaterThanOrEqualToConstantConstraint) {
            return construct((IsGreaterThanOrEqualToConstantConstraint) constraint, negate);
        } else if (constraint instanceof IsLessThanConstantConstraint) {
            return construct((IsLessThanConstantConstraint) constraint, negate);
        } else if (constraint instanceof IsLessThanOrEqualToConstantConstraint) {
            return construct((IsLessThanOrEqualToConstantConstraint) constraint, negate);
        } else if (constraint instanceof IsAfterConstantDateTimeConstraint) {
            return construct((IsAfterConstantDateTimeConstraint) constraint, negate);
        } else if (constraint instanceof IsAfterOrEqualToConstantDateTimeConstraint) {
            return construct((IsAfterOrEqualToConstantDateTimeConstraint) constraint, negate);
        } else if (constraint instanceof IsBeforeConstantDateTimeConstraint) {
            return construct((IsBeforeConstantDateTimeConstraint) constraint, negate);
        } else if (constraint instanceof IsBeforeOrEqualToConstantDateTimeConstraint) {
            return construct((IsBeforeOrEqualToConstantDateTimeConstraint) constraint, negate);
        } else if (constraint instanceof IsGranularToNumericConstraint) {
            return construct((IsGranularToNumericConstraint) constraint, negate);
        } else if (constraint instanceof IsGranularToDateConstraint) {
            return construct((IsGranularToDateConstraint) constraint, negate);
        } else if (constraint instanceof IsNullConstraint) {
            return constructIsNull(negate);
        } else if (constraint instanceof MatchesRegexConstraint) {
            return construct((MatchesRegexConstraint) constraint, negate);
        } else if (constraint instanceof ContainsRegexConstraint) {
            return construct((ContainsRegexConstraint) constraint, negate);
        } else if (constraint instanceof MatchesStandardConstraint) {
            return construct((MatchesStandardConstraint) constraint, negate);
        } else if (constraint instanceof IsOfTypeConstraint) {
            return construct((IsOfTypeConstraint) constraint, negate);
        } else if (constraint instanceof StringHasLengthConstraint) {
            return construct((StringHasLengthConstraint) constraint, negate);
        } else if (constraint instanceof IsStringLongerThanConstraint) {
            return construct((IsStringLongerThanConstraint) constraint, negate);
        } else if (constraint instanceof IsStringShorterThanConstraint) {
            return construct((IsStringShorterThanConstraint) constraint, negate);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private FieldSpec construct(IsInSetConstraint constraint, boolean negate) {
        if (negate) {
            return FieldSpec.Empty.withBlacklist(constraint.legalValuesWithoutFrequency());
        }

        return FieldSpec.Empty.withWhitelist(constraint.legalValues);
    }

    private FieldSpec construct(EqualToConstraint constraint, boolean negate) {
        if (negate) {
            return FieldSpec.Empty.withBlacklist(Collections.singleton(constraint.value));
        }

        return FieldSpec.Empty
            .withWhitelist(FrequencyDistributedSet.singleton(constraint.value))
            .withNotNull();
    }

    private FieldSpec constructIsNull(boolean negate) {
        if (negate) {
            return FieldSpec.Empty.withNotNull();
        }

        return FieldSpec.NullOnly;
    }

    private FieldSpec construct(IsOfTypeConstraint constraint, boolean negate) {
        if (negate) {
            throw new UnsupportedOperationException("cannot negate types");
        }

        return FieldSpec.Empty.withType(constraint.requiredType);
    }

    private FieldSpec construct(IsGreaterThanConstantConstraint constraint, boolean negate) {
        return constructGreaterThanConstraint(constraint.referenceValue, false, negate);
    }

    private FieldSpec construct(IsGreaterThanOrEqualToConstantConstraint constraint, boolean negate) {
        return constructGreaterThanConstraint(constraint.referenceValue, true, negate);
    }

    private FieldSpec constructGreaterThanConstraint(Number limitValue, boolean inclusive, boolean negate) {
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

        return FieldSpec.Empty.withNumericRestrictions(numericRestrictions);
    }

    private FieldSpec construct(IsLessThanConstantConstraint constraint, boolean negate) {
        return constructLessThanConstraint(constraint.referenceValue, false, negate);
    }

    private FieldSpec construct(IsLessThanOrEqualToConstantConstraint constraint, boolean negate) {
        return constructLessThanConstraint(constraint.referenceValue, true, negate);
    }

    private FieldSpec constructLessThanConstraint(Number limitValue, boolean inclusive, boolean negate) {
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

        return FieldSpec.Empty.withNumericRestrictions(numericRestrictions);
    }

    private FieldSpec construct(IsGranularToNumericConstraint constraint, boolean negate) {
        if (negate) {
            // negated granularity is a future enhancement
            return FieldSpec.Empty;
        }

        return FieldSpec.Empty.withNumericRestrictions(new NumericRestrictions(constraint.granularity.getNumericGranularity().scale()));
    }

    private FieldSpec construct(IsGranularToDateConstraint constraint, boolean negate) {
        if (negate) {
            // negated granularity is a future enhancement
            return FieldSpec.Empty;
        }

        return FieldSpec.Empty.withDateTimeRestrictions(new DateTimeRestrictions(constraint.granularity.getGranularity()));
    }

    private FieldSpec construct(IsAfterConstantDateTimeConstraint constraint, boolean negate) {
        return constructIsAfterConstraint(constraint.referenceValue, false, negate);
    }

    private FieldSpec construct(IsAfterOrEqualToConstantDateTimeConstraint constraint, boolean negate) {
        return constructIsAfterConstraint(constraint.referenceValue, true, negate);
    }

    private FieldSpec constructIsAfterConstraint(OffsetDateTime limit, boolean inclusive, boolean negate) {
        final DateTimeRestrictions dateTimeRestrictions = new DateTimeRestrictions();

        if (negate) {
            dateTimeRestrictions.max = new DateTimeLimit(limit, !inclusive);
        } else {
            dateTimeRestrictions.min = new DateTimeLimit(limit, inclusive);
        }

        return FieldSpec.Empty.withDateTimeRestrictions(dateTimeRestrictions);
    }

    private FieldSpec construct(IsBeforeConstantDateTimeConstraint constraint, boolean negate) {
        return constructIsBeforeConstraint(constraint.referenceValue, false, negate);
    }

    private FieldSpec construct(IsBeforeOrEqualToConstantDateTimeConstraint constraint, boolean negate) {
        return constructIsBeforeConstraint(constraint.referenceValue, true, negate);
    }

    private FieldSpec constructIsBeforeConstraint(OffsetDateTime limit, boolean inclusive, boolean negate) {
        final DateTimeRestrictions dateTimeRestrictions = new DateTimeRestrictions();

        if (negate) {
            dateTimeRestrictions.min = new DateTimeLimit(limit, !inclusive);
        } else {
            dateTimeRestrictions.max = new DateTimeLimit(limit, inclusive);
        }

        return FieldSpec.Empty.withDateTimeRestrictions(dateTimeRestrictions);
    }

    private FieldSpec construct(MatchesRegexConstraint constraint, boolean negate) {
        return FieldSpec.Empty
            .withStringRestrictions(stringRestrictionsFactory.forStringMatching(constraint.regex, negate));
    }

    private FieldSpec construct(ContainsRegexConstraint constraint, boolean negate) {
        return FieldSpec.Empty
            .withStringRestrictions(stringRestrictionsFactory.forStringContaining(constraint.regex, negate));
    }

    private FieldSpec construct(MatchesStandardConstraint constraint, boolean negate) {
        if (constraint.standard.equals(RIC)) {
            return construct(new MatchesRegexConstraint(constraint.field, Pattern.compile(RIC.getRegex())), negate);
        }

        if (negate){
            return construct(new MatchesRegexConstraint(constraint.field, Pattern.compile(constraint.standard.getRegex())), negate);
        }

        return FieldSpec.Empty
            .withStringRestrictions(new MatchesStandardStringRestrictions(constraint.standard));
    }

    private FieldSpec construct(StringHasLengthConstraint constraint, boolean negate) {
        return FieldSpec.Empty
            .withStringRestrictions(stringRestrictionsFactory.forLength(constraint.referenceValue, negate));
    }

    private FieldSpec construct(IsStringShorterThanConstraint constraint, boolean negate) {
        return FieldSpec.Empty
            .withStringRestrictions(
                negate
                    ? stringRestrictionsFactory.forMinLength(constraint.referenceValue)
                    : stringRestrictionsFactory.forMaxLength(constraint.referenceValue - 1)
            );
    }

    private FieldSpec construct(IsStringLongerThanConstraint constraint, boolean negate) {
        return FieldSpec.Empty
            .withStringRestrictions(
                negate
                    ? stringRestrictionsFactory.forMaxLength(constraint.referenceValue)
                    : stringRestrictionsFactory.forMinLength(constraint.referenceValue + 1)
            );
    }

}
