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
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.restrictions.linear.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.regex.Pattern;

import static com.scottlogic.deg.generator.profile.constraints.atomic.StandardConstraintTypes.RIC;
import static com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory.createDateTimeRestrictions;
import static com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory.createNumericRestrictions;
import static com.scottlogic.deg.generator.utils.Defaults.*;

public class FieldSpecFactory {
    private final StringRestrictionsFactory stringRestrictionsFactory;

    @Inject
    public FieldSpecFactory(StringRestrictionsFactory stringRestrictionsFactory) {
        this.stringRestrictionsFactory = stringRestrictionsFactory;
    }

    public FieldSpec construct(AtomicConstraint constraint) {
        if (constraint instanceof ViolatedAtomicConstraint) {
            return construct(((ViolatedAtomicConstraint) constraint).violatedConstraint);
        } else if (constraint instanceof BlacklistConstraint) {
            return constructBlacklist((BlacklistConstraint) constraint);
        } else if (constraint instanceof NotContainsRegexConstraint) {
            return constructNotContainsRegex((NotContainsRegexConstraint) constraint);
        } else if (constraint instanceof NotEqualToConstraint) {
            return constructNotEqual((NotEqualToConstraint) constraint);
        } else if (constraint instanceof NotNullConstraint) {
            return constructNotNull((NotNullConstraint) constraint);
        } else if (constraint instanceof NotMatchesRegexConstraint) {
            return constructNotMatchesRegex((NotMatchesRegexConstraint) constraint);
        } else if (constraint instanceof NotMatchesStandardConstraint) {
            return constructNotMatchesStandard((NotMatchesStandardConstraint) constraint);
        } else if (constraint instanceof NotStringLengthConstraint) {
            return constructNotStringLength((NotStringLengthConstraint) constraint);
        } else if (constraint instanceof IsInSetConstraint) {
            return construct((IsInSetConstraint) constraint);
        } else if (constraint instanceof EqualToConstraint) {
            return construct((EqualToConstraint) constraint);
        } else if (constraint instanceof IsGreaterThanConstantConstraint) {
            return construct((IsGreaterThanConstantConstraint) constraint);
        } else if (constraint instanceof IsGreaterThanOrEqualToConstantConstraint) {
            return construct((IsGreaterThanOrEqualToConstantConstraint) constraint);
        } else if (constraint instanceof IsLessThanConstantConstraint) {
            return construct((IsLessThanConstantConstraint) constraint);
        } else if (constraint instanceof IsLessThanOrEqualToConstantConstraint) {
            return construct((IsLessThanOrEqualToConstantConstraint) constraint);
        } else if (constraint instanceof IsAfterConstantDateTimeConstraint) {
            return construct((IsAfterConstantDateTimeConstraint) constraint);
        } else if (constraint instanceof IsAfterOrEqualToConstantDateTimeConstraint) {
            return construct((IsAfterOrEqualToConstantDateTimeConstraint) constraint);
        } else if (constraint instanceof IsBeforeConstantDateTimeConstraint) {
            return construct((IsBeforeConstantDateTimeConstraint) constraint);
        } else if (constraint instanceof IsBeforeOrEqualToConstantDateTimeConstraint) {
            return construct((IsBeforeOrEqualToConstantDateTimeConstraint) constraint);
        } else if (constraint instanceof IsGranularToNumericConstraint) {
            return construct((IsGranularToNumericConstraint) constraint);
        } else if (constraint instanceof IsGranularToDateConstraint) {
            return construct((IsGranularToDateConstraint) constraint);
        } else if (constraint instanceof IsNullConstraint) {
            return constructIsNull();
        } else if (constraint instanceof MatchesRegexConstraint) {
            return construct((MatchesRegexConstraint) constraint);
        } else if (constraint instanceof ContainsRegexConstraint) {
            return construct((ContainsRegexConstraint) constraint);
        } else if (constraint instanceof MatchesStandardConstraint) {
            return construct((MatchesStandardConstraint) constraint);
        } else if (constraint instanceof StringHasLengthConstraint) {
            return construct((StringHasLengthConstraint) constraint);
        } else if (constraint instanceof IsStringLongerThanConstraint) {
            return construct((IsStringLongerThanConstraint) constraint);
        } else if (constraint instanceof IsStringShorterThanConstraint) {
            return construct((IsStringShorterThanConstraint) constraint);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private FieldSpec constructNotStringLength(NotStringLengthConstraint constraint) {
        return FieldSpec.fromRestriction(stringRestrictionsFactory.forLength(constraint.referenceValue, true));
    }

    private FieldSpec constructNotMatchesStandard(NotMatchesStandardConstraint constraint) {
        return construct(new MatchesRegexConstraint(constraint.field, Pattern.compile(constraint.standard.getRegex())));
    }

    private FieldSpec constructNotMatchesRegex(NotMatchesRegexConstraint constraint) {
        return FieldSpec.fromRestriction(stringRestrictionsFactory.forStringMatching(constraint.regex, true));
    }

    private FieldSpec constructNotContainsRegex(NotContainsRegexConstraint constraint) {
        return FieldSpec.fromRestriction(stringRestrictionsFactory.forStringContaining(constraint.regex, true));
    }

    private FieldSpec constructNotNull(NotNullConstraint constraint) {
        return FieldSpec.empty().withNotNull();
    }

    private FieldSpec constructNotEqual(NotEqualToConstraint constraint) {
        return FieldSpec.empty().withBlacklist(Collections.singleton(constraint.value));
    }

    private FieldSpec constructBlacklist(BlacklistConstraint constraint) {
        return FieldSpec.empty().withBlacklist(new HashSet<>(constraint.legalValues.list()));
    }

    private FieldSpec construct(IsInSetConstraint constraint) {
        return FieldSpec.fromList(constraint.legalValues);
    }

    private FieldSpec construct(EqualToConstraint constraint) {
        return FieldSpec.fromList(DistributedList.singleton(constraint.value))
            .withNotNull();
    }

    private FieldSpec constructIsNull() {
        return FieldSpec.nullOnly();
    }

    private FieldSpec construct(IsGreaterThanConstantConstraint constraint) {
        return constructGreaterThanConstraint(constraint.referenceValue, false);
    }

    private FieldSpec construct(IsGreaterThanOrEqualToConstantConstraint constraint) {
        return constructGreaterThanConstraint(constraint.referenceValue, true);
    }

    private FieldSpec constructGreaterThanConstraint(BigDecimal limit, boolean inclusive) {
        LinearRestrictions<BigDecimal> numericRestrictions = createNumericRestrictions(new Limit<>(limit, inclusive), NUMERIC_MAX_LIMIT);
        return FieldSpec.fromRestriction(numericRestrictions);
    }

    private FieldSpec construct(IsLessThanConstantConstraint constraint) {
        return constructLessThanConstraint(constraint.referenceValue, false);
    }

    private FieldSpec construct(IsLessThanOrEqualToConstantConstraint constraint) {
        return constructLessThanConstraint(constraint.referenceValue, true);
    }

    private FieldSpec constructLessThanConstraint(BigDecimal limit, boolean inclusive) {
        final LinearRestrictions<BigDecimal> numericRestrictions = createNumericRestrictions(NUMERIC_MIN_LIMIT, new Limit<>(limit, inclusive));
        return FieldSpec.fromRestriction(numericRestrictions);
    }

    private FieldSpec construct(IsGranularToNumericConstraint constraint) {
        return FieldSpec.fromRestriction(createNumericRestrictions(NUMERIC_MIN_LIMIT, NUMERIC_MAX_LIMIT, constraint.granularity.getNumericGranularity().scale()));
    }

    private FieldSpec construct(IsGranularToDateConstraint constraint) {
        return FieldSpec.fromRestriction(createDateTimeRestrictions(DATETIME_MIN_LIMIT, DATETIME_MAX_LIMIT, constraint.granularity.getGranularity()));
    }

    private FieldSpec construct(IsAfterConstantDateTimeConstraint constraint) {
        return constructIsAfterConstraint(constraint.referenceValue, false);
    }

    private FieldSpec construct(IsAfterOrEqualToConstantDateTimeConstraint constraint) {
        return constructIsAfterConstraint(constraint.referenceValue, true);
    }

    private FieldSpec constructIsAfterConstraint(OffsetDateTime limit, boolean inclusive) {
        final LinearRestrictions<OffsetDateTime> dateTimeRestrictions = createDateTimeRestrictions(new Limit<>(limit, inclusive), DATETIME_MAX_LIMIT);
        return FieldSpec.fromRestriction(dateTimeRestrictions);
    }

    private FieldSpec construct(IsBeforeConstantDateTimeConstraint constraint) {
        return constructIsBeforeConstraint(constraint.referenceValue, false);
    }

    private FieldSpec construct(IsBeforeOrEqualToConstantDateTimeConstraint constraint) {
        return constructIsBeforeConstraint(constraint.referenceValue, true);
    }

    private FieldSpec constructIsBeforeConstraint(OffsetDateTime limit, boolean inclusive) {
        final LinearRestrictions<OffsetDateTime> dateTimeRestrictions = createDateTimeRestrictions(DATETIME_MIN_LIMIT, new Limit<>(limit, inclusive));
        return FieldSpec.fromRestriction(dateTimeRestrictions);
    }

    private FieldSpec construct(MatchesRegexConstraint constraint) {
        return FieldSpec.fromRestriction(stringRestrictionsFactory.forStringMatching(constraint.regex, false));
    }

    private FieldSpec construct(ContainsRegexConstraint constraint) {
        return FieldSpec.fromRestriction(stringRestrictionsFactory.forStringContaining(constraint.regex, false));
    }

    private FieldSpec construct(MatchesStandardConstraint constraint) {
        if (constraint.standard.equals(RIC)) {
            return construct(new MatchesRegexConstraint(constraint.field, Pattern.compile(RIC.getRegex())));
        }

        return FieldSpec.fromRestriction(new MatchesStandardStringRestrictions(constraint.standard));
    }

    private FieldSpec construct(StringHasLengthConstraint constraint) {
        return FieldSpec.fromRestriction(stringRestrictionsFactory.forLength(constraint.referenceValue, false));
    }

    private FieldSpec construct(IsStringShorterThanConstraint constraint) {
        return FieldSpec.fromRestriction(stringRestrictionsFactory.forMaxLength(constraint.referenceValue - 1));
    }

    private FieldSpec construct(IsStringLongerThanConstraint constraint) {
        return FieldSpec.fromRestriction(stringRestrictionsFactory.forMinLength(constraint.referenceValue + 1));
    }

}
