package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.generation.RegexStringGenerator;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.generation.StringGenerator;
import com.scottlogic.deg.generator.utils.NumberUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FieldSpecFactory {
    public FieldSpec construct(AtomicConstraint constraint) {
        return construct(constraint, false, false);
    }

    public FieldSpec toMustContainRestrictionFieldSpec(Collection<AtomicConstraint> constraints) {
        if (constraints.isEmpty()) return FieldSpec.Empty;
        return FieldSpec.Empty.withMustContainRestriction(
            new MustContainRestriction(
                constraints.stream().map(this::construct).collect(Collectors.toSet())
            )
        );
    }

    private FieldSpec construct(AtomicConstraint constraint, boolean negate, boolean violated) {
        if (constraint instanceof ViolatedAtomicConstraint){
            return construct(((ViolatedAtomicConstraint)constraint).violatedConstraint, negate, true);
        } else if (constraint instanceof NotConstraint) {
            return construct(((NotConstraint) constraint).negatedConstraint, !negate, violated);
        } else if (constraint instanceof IsInSetConstraint) {
            return construct((IsInSetConstraint) constraint, negate, violated);
        } else if (constraint instanceof IsEqualToConstantConstraint) {
            return construct((IsEqualToConstantConstraint) constraint, negate, violated);
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
        } else if (constraint instanceof IsGranularToConstraint) {
            return construct((IsGranularToConstraint) constraint, negate, violated);
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

    private FieldSpec construct(IsEqualToConstantConstraint constraint, boolean negate, boolean violated) {
        return construct(
            new IsInSetConstraint(
                constraint.field,
                Collections.singleton(constraint.requiredValue),
                constraint.getRules()
            ),
            negate,
            violated);
    }

    private FieldSpec construct(IsInSetConstraint constraint, boolean negate, boolean violated) {
        return FieldSpec.Empty.withSetRestrictions(
                negate
                    ? SetRestrictions.fromBlacklist(constraint.legalValues)
                    : SetRestrictions.fromWhitelist(constraint.legalValues),
            FieldSpecSource.fromConstraint(constraint, negate, violated));
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
            typeRestrictions = DataTypeRestrictions.all.except(constraint.requiredType);
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

    private FieldSpec construct(IsGranularToConstraint constraint, boolean negate, boolean violated) {
        if (negate) {
            // it's not worth much effort to figure out how to negate a formatting constraint - let's just make it a no-op
            return FieldSpec.Empty;
        }

        return FieldSpec.Empty.withGranularityRestrictions(
            new GranularityRestrictions(constraint.granularity),
            FieldSpecSource.fromConstraint(constraint, false, violated));
    }

    private FieldSpec construct(IsAfterConstantDateTimeConstraint constraint, boolean negate, boolean violated) {
        return constructIsAfterConstraint(constraint.referenceValue, false, negate, constraint, violated);
    }

    private FieldSpec construct(IsAfterOrEqualToConstantDateTimeConstraint constraint, boolean negate, boolean violated) {
        return constructIsAfterConstraint(constraint.referenceValue, true, negate, constraint, violated);
    }

    private FieldSpec constructIsAfterConstraint(LocalDateTime limit, boolean inclusive, boolean negate, AtomicConstraint constraint, boolean violated) {
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

    private FieldSpec constructIsBeforeConstraint(LocalDateTime limit, boolean inclusive, boolean negate, AtomicConstraint constraint, boolean violated) {
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
        return constructPattern(constraint.regex, negate, true, constraint, violated);
    }

    private FieldSpec construct(ContainsRegexConstraint constraint, boolean negate, boolean violated) {
        return constructPattern(constraint.regex, negate, false, constraint, violated);
    }

    private FieldSpec construct(MatchesStandardConstraint constraint, boolean negate, boolean violated) {
        return constructGenerator(constraint.standard, negate, constraint, violated);
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
        final Pattern regex = Pattern.compile(String.format(".{%s}", constraint.referenceValue));
        return constructPattern(regex, negate, true, constraint, violated);
    }

    private FieldSpec construct(IsStringShorterThanConstraint constraint, boolean negate, boolean violated) {
        final Pattern regex = Pattern.compile(String.format(".{0,%d}", constraint.referenceValue - 1));
        return constructPattern(regex, negate, true, constraint, violated);
    }

    private FieldSpec construct(IsStringLongerThanConstraint constraint, boolean negate, boolean violated) {
        final Pattern regex = Pattern.compile(String.format(".{%d,}", constraint.referenceValue + 1));
        return constructPattern(regex, negate, true, constraint, violated);
    }

    private FieldSpec constructPattern(Pattern pattern, boolean negate, boolean matchFullString, AtomicConstraint constraint, boolean violated) {
        return constructGenerator(new RegexStringGenerator(pattern.toString(), matchFullString), negate, constraint, violated);
    }

    private FieldSpec constructGenerator(StringGenerator generator, boolean negate, AtomicConstraint constraint, boolean violated) {
        final StringRestrictions stringRestrictions = new StringRestrictions();

        stringRestrictions.stringGenerator = negate
            ? generator.complement()
            : generator;

        return FieldSpec.Empty.withStringRestrictions(
            stringRestrictions,
            FieldSpecSource.fromConstraint(constraint, negate, violated));
    }
}
