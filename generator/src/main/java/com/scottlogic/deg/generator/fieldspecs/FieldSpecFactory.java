package com.scottlogic.deg.generator.fieldspecs;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.common.profile.constraintdetail.Nullness;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.common.util.NumberUtils;
import com.scottlogic.deg.generator.restrictions.set.SetRestrictions;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

public class FieldSpecFactory {
    private final StringRestrictionsFactory stringRestrictionsFactory;

    @Inject
    public FieldSpecFactory(StringRestrictionsFactory stringRestrictionsFactory) {
        this.stringRestrictionsFactory = stringRestrictionsFactory;
    }

    public FieldSpec construct(AtomicConstraint constraint) {
        return construct(constraint, false, false);
    }

    private FieldSpec construct(AtomicConstraint constraint, boolean negate, boolean violated) {
        if (constraint instanceof ViolatedAtomicConstraint){
            return construct(((ViolatedAtomicConstraint)constraint).violatedConstraint, negate, true);
        } else if (constraint instanceof NotConstraint) {
            return construct(((NotConstraint) constraint).negatedConstraint, !negate, violated);
        } else if (constraint instanceof IsInSetConstraint) {
            return construct((IsInSetConstraint) constraint, negate, violated);
        } else if (constraint instanceof IsGreaterThanConstantConstraint) {
            return construct((IsGreaterThanConstantConstraint) constraint, negate, violated);
        } else if (constraint instanceof IsGreaterThanOrEqualToConstantConstraint) {
            return construct((IsGreaterThanOrEqualToConstantConstraint) constraint, negate, violated);
        } else if (constraint instanceof IsLessThanConstantConstraint) {
            return construct((IsLessThanConstantConstraint) constraint, negate, violated);
        } else if (constraint instanceof IsLessThanOrEqualToConstantConstraint) {
            return construct((IsLessThanOrEqualToConstantConstraint) constraint, negate, violated);
        } else if (constraint instanceof IsAfterConstantDateTimeConstraint) {
            return construct((IsAfterConstantDateTimeConstraint) constraint, negate, violated);
        } else if (constraint instanceof IsAfterOrEqualToConstantDateTimeConstraint) {
            return construct((IsAfterOrEqualToConstantDateTimeConstraint) constraint, negate, violated);
        } else if (constraint instanceof IsBeforeConstantDateTimeConstraint) {
            return construct((IsBeforeConstantDateTimeConstraint) constraint, negate, violated);
        } else if (constraint instanceof IsBeforeOrEqualToConstantDateTimeConstraint) {
            return construct((IsBeforeOrEqualToConstantDateTimeConstraint) constraint, negate, violated);
        } else if (constraint instanceof IsGranularToNumericConstraint) {
            return construct((IsGranularToNumericConstraint) constraint, negate, violated);
        } else if (constraint instanceof IsGranularToDateConstraint){
            return construct((IsGranularToDateConstraint) constraint, negate, violated);
        } else if (constraint instanceof IsNullConstraint) {
            return constructIsNull(negate, constraint, violated);
        } else if (constraint instanceof MatchesRegexConstraint) {
            return construct((MatchesRegexConstraint) constraint, negate, violated);
        } else if (constraint instanceof ContainsRegexConstraint) {
            return construct((ContainsRegexConstraint) constraint, negate, violated);
        } else if (constraint instanceof MatchesStandardConstraint) {
            return construct((MatchesStandardConstraint) constraint, negate, violated);
        } else if (constraint instanceof IsOfTypeConstraint) {
            return construct((IsOfTypeConstraint) constraint, negate, violated);
        } else if (constraint instanceof FormatConstraint) {
            return construct((FormatConstraint) constraint, negate, violated);
        } else if (constraint instanceof StringHasLengthConstraint) {
            return construct((StringHasLengthConstraint) constraint, negate, violated);
        } else if (constraint instanceof IsStringLongerThanConstraint) {
            return construct((IsStringLongerThanConstraint) constraint, negate, violated);
        } else if (constraint instanceof IsStringShorterThanConstraint) {
            return construct((IsStringShorterThanConstraint) constraint, negate, violated);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private FieldSpec construct(IsInSetConstraint constraint, boolean negate, boolean violated) {
        return FieldSpec.Empty.withSetRestrictions(
                negate
                    ? negatedSet(constraint, constraint.legalValues)
                    : SetRestrictions.fromWhitelist(constraint.legalValues),
            FieldSpecSource.fromConstraint(constraint, negate, violated));
    }

    private SetRestrictions negatedSet(IsInSetConstraint constraint, Set<Object> values) {
        return (constraint instanceof IsInNameSetConstraint)
            ? SetRestrictions.allowNoValues()
            : SetRestrictions.fromBlacklist(values);
    }

    private FieldSpec constructIsNull(boolean negate, AtomicConstraint constraint, boolean violated) {
        final NullRestrictions nullRestrictions = new NullRestrictions();

        nullRestrictions.nullness = negate
            ? Nullness.MUST_NOT_BE_NULL
            : Nullness.MUST_BE_NULL;

        return FieldSpec.Empty.withNullRestrictions(
            nullRestrictions,
            FieldSpecSource.fromConstraint(constraint, negate, violated));
    }

    private FieldSpec construct(IsOfTypeConstraint constraint, boolean negate, boolean violated) {
        final TypeRestrictions typeRestrictions;

        if (negate) {
            typeRestrictions = DataTypeRestrictions.ALL_TYPES_PERMITTED.except(constraint.requiredType);
        } else {
            typeRestrictions = DataTypeRestrictions.createFromWhiteList(constraint.requiredType);
        }

        return FieldSpec.Empty.withTypeRestrictions(
            typeRestrictions,
            FieldSpecSource.fromConstraint(constraint, negate, violated));
    }

    private FieldSpec construct(IsGreaterThanConstantConstraint constraint, boolean negate, boolean violated) {
        return constructGreaterThanConstraint(constraint.referenceValue, false, negate, constraint, violated);
    }

    private FieldSpec construct(IsGreaterThanOrEqualToConstantConstraint constraint, boolean negate, boolean violated) {
        return constructGreaterThanConstraint(constraint.referenceValue, true, negate, constraint, violated);
    }

    private FieldSpec constructGreaterThanConstraint(Number limitValue, boolean inclusive, boolean negate, AtomicConstraint constraint, boolean violated) {
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

        return FieldSpec.Empty.withNumericRestrictions(
            numericRestrictions,
            FieldSpecSource.fromConstraint(constraint, negate, violated));
    }

    private FieldSpec construct(IsLessThanConstantConstraint constraint, boolean negate, boolean violated) {
        return constructLessThanConstraint(constraint.referenceValue, false, negate, constraint, violated);
    }

    private FieldSpec construct(IsLessThanOrEqualToConstantConstraint constraint, boolean negate, boolean violated) {
        return constructLessThanConstraint(constraint.referenceValue, true, negate, constraint, violated);
    }

    private FieldSpec constructLessThanConstraint(Number limitValue, boolean inclusive, boolean negate, AtomicConstraint constraint, boolean violated) {
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

        return FieldSpec.Empty.withNumericRestrictions(
            numericRestrictions,
            FieldSpecSource.fromConstraint(constraint, negate, violated));
    }

    private FieldSpec construct(IsGranularToNumericConstraint constraint, boolean negate, boolean violated) {
        if (negate) {
            // negated granularity is a future enhancement
            return FieldSpec.Empty;
        }

        return FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions(constraint.granularity.getNumericGranularity().scale()),
            FieldSpecSource.fromConstraint(constraint, false, violated));
    }

    private FieldSpec construct(IsGranularToDateConstraint constraint, boolean negate, boolean violated) {
        if (negate) {
            // negated granularity is a future enhancement
            return FieldSpec.Empty;
        }

        return FieldSpec.Empty.withDateTimeRestrictions(
            new DateTimeRestrictions(constraint.granularity.getGranularity()),
            FieldSpecSource.fromConstraint(constraint, false, violated));
    }

    private FieldSpec construct(IsAfterConstantDateTimeConstraint constraint, boolean negate, boolean violated) {
        return constructIsAfterConstraint(constraint.referenceValue, false, negate, constraint, violated);
    }

    private FieldSpec construct(IsAfterOrEqualToConstantDateTimeConstraint constraint, boolean negate, boolean violated) {
        return constructIsAfterConstraint(constraint.referenceValue, true, negate, constraint, violated);
    }

    private FieldSpec constructIsAfterConstraint(OffsetDateTime limit, boolean inclusive, boolean negate, AtomicConstraint constraint, boolean violated) {
        final DateTimeRestrictions dateTimeRestrictions = new DateTimeRestrictions();

        if (negate) {
            dateTimeRestrictions.max = new DateTimeRestrictions.DateTimeLimit(limit, !inclusive);
        } else {
            dateTimeRestrictions.min = new DateTimeRestrictions.DateTimeLimit(limit, inclusive);
        }

        return FieldSpec.Empty.withDateTimeRestrictions(
            dateTimeRestrictions,
            FieldSpecSource.fromConstraint(constraint, negate, violated));
    }

    private FieldSpec construct(IsBeforeConstantDateTimeConstraint constraint, boolean negate, boolean violated) {
        return constructIsBeforeConstraint(constraint.referenceValue, false, negate, constraint, violated);
    }

    private FieldSpec construct(IsBeforeOrEqualToConstantDateTimeConstraint constraint, boolean negate, boolean violated) {
        return constructIsBeforeConstraint(constraint.referenceValue, true, negate, constraint, violated);
    }

    private FieldSpec constructIsBeforeConstraint(OffsetDateTime limit, boolean inclusive, boolean negate, AtomicConstraint constraint, boolean violated) {
        final DateTimeRestrictions dateTimeRestrictions = new DateTimeRestrictions();

        if (negate) {
            dateTimeRestrictions.min = new DateTimeRestrictions.DateTimeLimit(limit, !inclusive);
        } else {
            dateTimeRestrictions.max = new DateTimeRestrictions.DateTimeLimit(limit, inclusive);
        }

        return FieldSpec.Empty.withDateTimeRestrictions(
            dateTimeRestrictions,
            FieldSpecSource.fromConstraint(constraint, negate, violated));
    }

    private FieldSpec construct(MatchesRegexConstraint constraint, boolean negate, boolean violated) {
        return FieldSpec.Empty
            .withStringRestrictions(
                stringRestrictionsFactory.forStringMatching(constraint.regex, negate),
                FieldSpecSource.fromConstraint(constraint, negate, violated)
            );
    }

    private FieldSpec construct(ContainsRegexConstraint constraint, boolean negate, boolean violated) {
        return FieldSpec.Empty
            .withStringRestrictions(
                stringRestrictionsFactory.forStringContaining(constraint.regex, negate),
                FieldSpecSource.fromConstraint(constraint, negate, violated)
            );
    }

    private FieldSpec construct(MatchesStandardConstraint constraint, boolean negate, boolean violated) {
        return FieldSpec.Empty
            .withStringRestrictions(
                new MatchesStandardStringRestrictions(constraint.standard, negate),
                FieldSpecSource.fromConstraint(constraint, negate, violated)
            );
    }

    private FieldSpec construct(FormatConstraint constraint, boolean negate, boolean violated) {
        if (negate) {
            // it's not worth much effort to figure out how to negate a formatting constraint - let's just make it a no-op
            return FieldSpec.Empty;
        }

        final FormatRestrictions formatRestrictions = new FormatRestrictions();
        formatRestrictions.formatString = constraint.format;

        return FieldSpec.Empty.withFormatRestrictions(
            formatRestrictions,
            FieldSpecSource.fromConstraint(constraint, false, violated));
    }

    private FieldSpec construct(StringHasLengthConstraint constraint, boolean negate, boolean violated) {
        return FieldSpec.Empty
            .withStringRestrictions(
                stringRestrictionsFactory.forLength(constraint.referenceValue, negate),
                FieldSpecSource.fromConstraint(constraint, negate, violated)
            );
    }

    private FieldSpec construct(IsStringShorterThanConstraint constraint, boolean negate, boolean violated) {
        return FieldSpec.Empty
            .withStringRestrictions(
                negate
                    ? stringRestrictionsFactory.forMinLength(constraint.referenceValue)
                    : stringRestrictionsFactory.forMaxLength(constraint.referenceValue - 1),
                FieldSpecSource.fromConstraint(constraint, negate, violated)
            );
    }

    private FieldSpec construct(IsStringLongerThanConstraint constraint, boolean negate, boolean violated) {
        return FieldSpec.Empty
            .withStringRestrictions(
                negate
                    ? stringRestrictionsFactory.forMaxLength(constraint.referenceValue)
                    : stringRestrictionsFactory.forMinLength(constraint.referenceValue + 1),
                FieldSpecSource.fromConstraint(constraint, negate, violated)
            );
    }

}
