package com.scottlogic.deg.generator.fieldspecs;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.utils.NumberUtils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class FieldSpecFactory {
    private static final ConstraintChainDetail emptyChain = new ConstraintChainDetail();

    private final FieldSpecMerger fieldSpecMerger;

    @Inject
    public FieldSpecFactory(FieldSpecMerger fieldSpecMerger) {
        this.fieldSpecMerger = fieldSpecMerger;
    }

    public FieldSpec construct(AtomicConstraint constraint) {
        return construct(constraint, emptyChain);
    }

    public FieldSpec toMustContainRestrictionFieldSpec(FieldSpec rootFieldSpec, Collection<FieldSpec> decisionConstraints) {
        if (decisionConstraints == null || decisionConstraints.isEmpty()){
            return rootFieldSpec;
        }

        return rootFieldSpec.withMustContainRestriction(
            new MustContainRestriction(
                decisionConstraints.stream()
                    .map(decisionSpec -> fieldSpecMerger.merge(rootFieldSpec, decisionSpec))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet())
            )
        );
    }

    private FieldSpec construct(AtomicConstraint constraint, ConstraintChainDetail chain) {
        if (constraint instanceof ViolatedAtomicConstraint){
            return construct(((ViolatedAtomicConstraint)constraint).violatedConstraint, chain.violated());
        } else if (constraint instanceof NotConstraint) {
            return construct(((NotConstraint) constraint).negatedConstraint, chain.negated());
        } if (constraint instanceof SoftAtomicConstraint){
            return construct(((SoftAtomicConstraint)constraint).underlyingConstraint, chain.soft());
        } else if (constraint instanceof IsInSetConstraint) {
            return construct((IsInSetConstraint) constraint, chain);
        } else if (constraint instanceof IsGreaterThanConstantConstraint) {
            return construct((IsGreaterThanConstantConstraint) constraint, chain);
        } else if (constraint instanceof IsGreaterThanOrEqualToConstantConstraint) {
            return construct((IsGreaterThanOrEqualToConstantConstraint) constraint, chain);
        } else if (constraint instanceof IsLessThanConstantConstraint) {
            return construct((IsLessThanConstantConstraint) constraint, chain);
        } else if (constraint instanceof IsLessThanOrEqualToConstantConstraint) {
            return construct((IsLessThanOrEqualToConstantConstraint) constraint, chain);
        } else if (constraint instanceof IsAfterConstantDateTimeConstraint) {
            return construct((IsAfterConstantDateTimeConstraint) constraint, chain);
        } else if (constraint instanceof IsAfterOrEqualToConstantDateTimeConstraint) {
            return construct((IsAfterOrEqualToConstantDateTimeConstraint) constraint, chain);
        } else if (constraint instanceof IsBeforeConstantDateTimeConstraint) {
            return construct((IsBeforeConstantDateTimeConstraint) constraint, chain);
        } else if (constraint instanceof IsBeforeOrEqualToConstantDateTimeConstraint) {
            return construct((IsBeforeOrEqualToConstantDateTimeConstraint) constraint, chain);
        } else if (constraint instanceof IsGranularToConstraint) {
            return construct((IsGranularToConstraint) constraint, chain);
        } else if (constraint instanceof IsNullConstraint) {
            return constructIsNull(constraint, chain);
        } else if (constraint instanceof MatchesRegexConstraint) {
            return construct((MatchesRegexConstraint) constraint, chain);
        } else if (constraint instanceof ContainsRegexConstraint) {
            return construct((ContainsRegexConstraint) constraint, chain);
        } else if (constraint instanceof MatchesStandardConstraint) {
            return construct((MatchesStandardConstraint) constraint, chain);
        } else if (constraint instanceof IsOfTypeConstraint) {
            return construct((IsOfTypeConstraint) constraint, chain);
        } else if (constraint instanceof FormatConstraint) {
            return construct((FormatConstraint) constraint, chain);
        } else if (constraint instanceof StringHasLengthConstraint) {
            return construct((StringHasLengthConstraint) constraint, chain);
        } else if (constraint instanceof IsStringLongerThanConstraint) {
            return construct((IsStringLongerThanConstraint) constraint, chain);
        } else if (constraint instanceof IsStringShorterThanConstraint) {
            return construct((IsStringShorterThanConstraint) constraint, chain);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private FieldSpec construct(IsInSetConstraint constraint, ConstraintChainDetail chain) {
        return FieldSpec.Empty.withSetRestrictions(
                chain.negated
                    ? SetRestrictions.fromBlacklist(constraint.legalValues)
                    : SetRestrictions.fromWhitelist(constraint.legalValues),
            FieldSpecSource.fromConstraint(constraint, chain.negated, chain.violated));
    }

    private FieldSpec constructIsNull(AtomicConstraint constraint, ConstraintChainDetail chain) {
        final NullRestrictions nullRestrictions = new NullRestrictions();

        nullRestrictions.nullness = chain.negated
            ? Nullness.MUST_NOT_BE_NULL
            : Nullness.MUST_BE_NULL;

        return FieldSpec.Empty.withNullRestrictions(
            nullRestrictions,
            FieldSpecSource.fromConstraint(constraint, chain.negated, chain.violated));
    }

    private FieldSpec construct(IsOfTypeConstraint constraint, ConstraintChainDetail chain) {
        final TypeRestrictions typeRestrictions;

        if (chain.negated) {
            typeRestrictions = DataTypeRestrictions.ALL_TYPES_PERMITTED.except(constraint.requiredType);
        } else {
            typeRestrictions = DataTypeRestrictions.createFromWhiteList(constraint.requiredType);
        }

        return FieldSpec.Empty.withTypeRestrictions(
            typeRestrictions,
            FieldSpecSource.fromConstraint(constraint, chain.negated, chain.violated));
    }

    private FieldSpec construct(IsGreaterThanConstantConstraint constraint, ConstraintChainDetail chain) {
        return constructGreaterThanConstraint(constraint.referenceValue, false, constraint, chain);
    }

    private FieldSpec construct(IsGreaterThanOrEqualToConstantConstraint constraint, ConstraintChainDetail chain) {
        return constructGreaterThanConstraint(constraint.referenceValue, true, constraint, chain);
    }

    private FieldSpec constructGreaterThanConstraint(Number limitValue, boolean inclusive, AtomicConstraint constraint, ConstraintChainDetail chain) {
        final NumericRestrictions numericRestrictions = new NumericRestrictions();

        final BigDecimal limit = NumberUtils.coerceToBigDecimal(limitValue);
        if (chain.negated) {
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
            FieldSpecSource.fromConstraint(constraint, chain.negated, chain.violated));
    }

    private FieldSpec construct(IsLessThanConstantConstraint constraint, ConstraintChainDetail chain) {
        return constructLessThanConstraint(constraint.referenceValue, false, constraint, chain);
    }

    private FieldSpec construct(IsLessThanOrEqualToConstantConstraint constraint, ConstraintChainDetail chain) {
        return constructLessThanConstraint(constraint.referenceValue, true, constraint, chain);
    }

    private FieldSpec constructLessThanConstraint(Number limitValue, boolean inclusive, AtomicConstraint constraint, ConstraintChainDetail chain) {
        final NumericRestrictions numericRestrictions = new NumericRestrictions();
        final BigDecimal limit = NumberUtils.coerceToBigDecimal(limitValue);
        if (chain.negated) {
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
            FieldSpecSource.fromConstraint(constraint, chain.negated, chain.violated));
    }

    private FieldSpec construct(IsGranularToConstraint constraint, ConstraintChainDetail chain) {
        if (chain.negated) {
            // it's not worth much effort to figure out how to negate a formatting constraint - let's just make it a no-op
            return FieldSpec.Empty;
        }

        return FieldSpec.Empty.withNumericRestrictions(
            new NumericRestrictions(constraint.granularity),
            FieldSpecSource.fromConstraint(constraint, false, chain.violated));
    }

    private FieldSpec construct(IsAfterConstantDateTimeConstraint constraint, ConstraintChainDetail chain) {
        return constructIsAfterConstraint(constraint.referenceValue, false, constraint, chain);
    }

    private FieldSpec construct(IsAfterOrEqualToConstantDateTimeConstraint constraint, ConstraintChainDetail chain) {
        return constructIsAfterConstraint(constraint.referenceValue, true, constraint, chain);
    }

    private FieldSpec constructIsAfterConstraint(OffsetDateTime limit, boolean inclusive, AtomicConstraint constraint, ConstraintChainDetail chain) {
        final DateTimeRestrictions dateTimeRestrictions = new DateTimeRestrictions();

        if (chain.negated) {
            dateTimeRestrictions.max = new DateTimeRestrictions.DateTimeLimit(limit, !inclusive);
        } else {
            dateTimeRestrictions.min = new DateTimeRestrictions.DateTimeLimit(limit, inclusive);
        }

        return FieldSpec.Empty.withDateTimeRestrictions(
            dateTimeRestrictions,
            FieldSpecSource.fromConstraint(constraint, chain.negated, chain.violated));
    }

    private FieldSpec construct(IsBeforeConstantDateTimeConstraint constraint, ConstraintChainDetail chain) {
        return constructIsBeforeConstraint(constraint.referenceValue, false, constraint, chain);
    }

    private FieldSpec construct(IsBeforeOrEqualToConstantDateTimeConstraint constraint, ConstraintChainDetail chain) {
        return constructIsBeforeConstraint(constraint.referenceValue, true, constraint, chain);
    }

    private FieldSpec constructIsBeforeConstraint(OffsetDateTime limit, boolean inclusive, AtomicConstraint constraint, ConstraintChainDetail chain) {
        final DateTimeRestrictions dateTimeRestrictions = new DateTimeRestrictions();

        if (chain.negated) {
            dateTimeRestrictions.min = new DateTimeRestrictions.DateTimeLimit(limit, !inclusive);
        } else {
            dateTimeRestrictions.max = new DateTimeRestrictions.DateTimeLimit(limit, inclusive);
        }

        return FieldSpec.Empty.withDateTimeRestrictions(
            dateTimeRestrictions,
            FieldSpecSource.fromConstraint(constraint, chain.negated, chain.violated));
    }

    private FieldSpec construct(MatchesRegexConstraint constraint, ConstraintChainDetail chain) {
        return FieldSpec.Empty
            .withStringRestrictions(
                TextualRestrictions.withStringMatching(constraint.regex, chain.negated),
                FieldSpecSource.fromConstraint(constraint, chain.negated, chain.violated)
            );
    }

    private FieldSpec construct(ContainsRegexConstraint constraint, ConstraintChainDetail chain) {
        return FieldSpec.Empty
            .withStringRestrictions(
                TextualRestrictions.withStringContaining(constraint.regex, chain.negated),
                FieldSpecSource.fromConstraint(constraint, chain.negated, chain.violated)
            );
    }

    private FieldSpec construct(MatchesStandardConstraint constraint, ConstraintChainDetail chain) {
        return FieldSpec.Empty
            .withStringRestrictions(
                new MatchesStandardStringRestrictions(constraint.standard, chain.negated),
                FieldSpecSource.fromConstraint(constraint, chain.negated, chain.violated)
            );
    }

    private FieldSpec construct(FormatConstraint constraint, ConstraintChainDetail chain) {
        if (chain.negated) {
            // it's not worth much effort to figure out how to negate a formatting constraint - let's just make it a no-op
            return FieldSpec.Empty;
        }

        final FormatRestrictions formatRestrictions = new FormatRestrictions();
        formatRestrictions.formatString = constraint.format;

        return FieldSpec.Empty.withFormatRestrictions(
            formatRestrictions,
            FieldSpecSource.fromConstraint(constraint, false, chain.violated));
    }

    private FieldSpec construct(StringHasLengthConstraint constraint, ConstraintChainDetail chain) {
        return FieldSpec.Empty
            .withStringRestrictions(
                chain.negated
                    ? TextualRestrictions.withoutLength(constraint.referenceValue)
                    : TextualRestrictions.withLength(constraint.referenceValue, chain.soft),
                FieldSpecSource.fromConstraint(constraint, chain.negated, chain.violated)
            );
    }

    private FieldSpec construct(IsStringShorterThanConstraint constraint, ConstraintChainDetail chain) {
        return FieldSpec.Empty
            .withStringRestrictions(
                chain.negated
                    ? TextualRestrictions.withMinLength(constraint.referenceValue, chain.soft)
                    : TextualRestrictions.withMaxLength(constraint.referenceValue - 1, chain.soft),
                FieldSpecSource.fromConstraint(constraint, chain.negated, chain.violated)
            );
    }

    private FieldSpec construct(IsStringLongerThanConstraint constraint, ConstraintChainDetail chain) {
        return FieldSpec.Empty
            .withStringRestrictions(
                chain.negated
                    ? TextualRestrictions.withMaxLength(constraint.referenceValue, chain.soft)
                    : TextualRestrictions.withMinLength(constraint.referenceValue + 1, chain.soft),
                FieldSpecSource.fromConstraint(constraint, chain.negated, chain.violated)
            );
    }

    private static class ConstraintChainDetail {
        private final boolean negated;
        private final boolean violated;
        private final boolean soft;

        ConstraintChainDetail() {
            this(false, false, false);
        }

        private ConstraintChainDetail(boolean negated, boolean violated, boolean soft) {

            this.negated = negated;
            this.violated = violated;
            this.soft = soft;
        }

        ConstraintChainDetail negated(){
            return new ConstraintChainDetail(true, violated, soft);
        }

        ConstraintChainDetail violated(){
            return new ConstraintChainDetail(negated, true, soft);
        }

        ConstraintChainDetail soft(){
            return new ConstraintChainDetail(negated, violated, true);
        }
    }
}
