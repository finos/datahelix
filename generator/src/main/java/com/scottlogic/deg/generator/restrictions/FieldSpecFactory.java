package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.*;
import com.scottlogic.deg.generator.reducer.AutomatonFactory;
import com.scottlogic.deg.generator.restrictions.TypeConstraintFieldSpecStrategyFactory.TypeConstraintFieldSpecStrategy;
import dk.brics.automaton.Automaton;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

public class FieldSpecFactory {
    private final AutomatonFactory automatonFactory = new AutomatonFactory();

    public FieldSpec construct(IConstraint constraint) {
        final FieldSpec fieldSpec = new FieldSpec();
        apply(fieldSpec, constraint, false);
        return fieldSpec;
    }

    private void apply(FieldSpec fieldSpec, IConstraint constraint, boolean negate) {
        if (constraint instanceof NotConstraint) {
            apply(fieldSpec, ((NotConstraint) constraint).negatedConstraint, !negate);
        } else if (constraint instanceof IsInSetConstraint) {
            apply(fieldSpec, (IsInSetConstraint) constraint, negate);
        } else if (constraint instanceof IsEqualToConstantConstraint) {
            apply(fieldSpec, (IsEqualToConstantConstraint) constraint, negate);
        } else if (constraint instanceof IsGreaterThanConstantConstraint) {
            apply(fieldSpec, (IsGreaterThanConstantConstraint) constraint, negate);
        } else if (constraint instanceof IsGreaterThanOrEqualToConstantConstraint) {
            apply(fieldSpec, (IsGreaterThanOrEqualToConstantConstraint) constraint, negate);
        } else if (constraint instanceof IsLessThanConstantConstraint) {
            apply(fieldSpec, (IsLessThanConstantConstraint) constraint, negate);
        } else if (constraint instanceof IsLessThanOrEqualToConstantConstraint) {
            apply(fieldSpec, (IsLessThanOrEqualToConstantConstraint) constraint, negate);
        } else if (constraint instanceof IsAfterConstantDateTimeConstraint) {
            apply(fieldSpec, (IsAfterConstantDateTimeConstraint) constraint, negate);
        } else if (constraint instanceof IsAfterOrEqualToConstantDateTimeConstraint) {
            apply(fieldSpec, (IsAfterOrEqualToConstantDateTimeConstraint) constraint, negate);
        } else if (constraint instanceof IsBeforeConstantDateTimeConstraint) {
            apply(fieldSpec, (IsBeforeConstantDateTimeConstraint) constraint, negate);
        } else if (constraint instanceof IsBeforeOrEqualToConstantDateTimeConstraint) {
            apply(fieldSpec, (IsBeforeOrEqualToConstantDateTimeConstraint) constraint, negate);
        } else if (constraint instanceof IsNullConstraint) {
            apply(fieldSpec, (IsNullConstraint) constraint, negate);
        } else if (constraint instanceof MatchesRegexConstraint) {
            apply(fieldSpec, (MatchesRegexConstraint) constraint, negate);
        } else if (constraint instanceof IsOfTypeConstraint) {
            apply(fieldSpec, (IsOfTypeConstraint) constraint, negate);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private void apply(FieldSpec fieldSpec, IsEqualToConstantConstraint constraint, boolean negate) {
        apply(
                fieldSpec,
                new IsInSetConstraint(
                        constraint.field,
                        Collections.singleton(constraint.requiredValue)
                ),
                negate
        );
    }

    private void apply(FieldSpec fieldSpec, IsInSetConstraint constraint, boolean negate) {
        SetRestrictions setRestrictions = fieldSpec.getSetRestrictions();
        if (setRestrictions == null) {
            setRestrictions = new SetRestrictions();
            fieldSpec.setSetRestrictions(setRestrictions);
        }
        if (negate) {
            setRestrictions.blacklist = constraint.legalValues;
        } else {
            setRestrictions.whitelist = constraint.legalValues;
        }
    }

    private void apply(FieldSpec fieldSpec, IsNullConstraint constraint, boolean negate) {
        NullRestrictions nullRestrictions = fieldSpec.getNullRestrictions();
        if (nullRestrictions == null) {
            nullRestrictions = new NullRestrictions();
            fieldSpec.setNullRestrictions(nullRestrictions);
        }
        nullRestrictions.nullness = negate
                ? NullRestrictions.Nullness.MustNotBeNull
                : NullRestrictions.Nullness.MustBeNull;
    }

    private void apply(FieldSpec fieldSpec, IsOfTypeConstraint constraint, boolean negate) {
        if (negate) {
            throw new UnsupportedOperationException();
        }
        final TypeConstraintFieldSpecStrategy strategy = TypeConstraintFieldSpecStrategyFactory.getStrategy(constraint);
        strategy.accept(fieldSpec);
    }

    private void apply(FieldSpec fieldSpec, IsGreaterThanConstantConstraint constraint, boolean negate) {
        applyGreaterThanConstraint(fieldSpec, constraint.referenceValue, false, negate);
    }

    private void apply(FieldSpec fieldSpec, IsGreaterThanOrEqualToConstantConstraint constraint, boolean negate) {
        applyGreaterThanConstraint(fieldSpec, constraint.referenceValue, true, negate);
    }

    private void applyGreaterThanConstraint(FieldSpec fieldSpec, Number limitValue, boolean inclusive, boolean negate) {
        NumericRestrictions numericRestrictions = fieldSpec.getNumericRestrictions();
        if (numericRestrictions == null) {
            numericRestrictions = new NumericRestrictions();
            fieldSpec.setNumericRestrictions(numericRestrictions);
        }
        final BigDecimal limit = numberToBigDecimal(limitValue);
        if (negate) {
            numericRestrictions.max = new NumericRestrictions.NumericLimit(
                    limit,
                    !inclusive
            );
        } else {
            numericRestrictions.min = new NumericRestrictions.NumericLimit(
                    limit,
                    inclusive
            );
        }
    }

    private void apply(FieldSpec fieldSpec, IsLessThanConstantConstraint constraint, boolean negate) {
        applyLessThanConstraint(fieldSpec, constraint.referenceValue, false, negate);
    }

    private void apply(FieldSpec fieldSpec, IsLessThanOrEqualToConstantConstraint constraint, boolean negate) {
        applyLessThanConstraint(fieldSpec, constraint.referenceValue, true, negate);
    }

    private void applyLessThanConstraint(FieldSpec fieldSpec, Number limitValue, boolean inclusive, boolean negate) {
        NumericRestrictions numericRestrictions = fieldSpec.getNumericRestrictions();
        if (numericRestrictions == null) {
            numericRestrictions = new NumericRestrictions();
            fieldSpec.setNumericRestrictions(numericRestrictions);
        }
        final BigDecimal limit = numberToBigDecimal(limitValue);
        if (negate) {
            numericRestrictions.min = new NumericRestrictions.NumericLimit(
                    limit,
                    !inclusive
            );
        } else {
            numericRestrictions.max = new NumericRestrictions.NumericLimit(
                    limit,
                    inclusive
            );
        }
    }

    private void apply(FieldSpec fieldSpec, IsAfterConstantDateTimeConstraint constraint, boolean negate) {
        applyIsAfterConstraint(fieldSpec, constraint.referenceValue, false, negate);
    }

    private void apply(FieldSpec fieldSpec, IsAfterOrEqualToConstantDateTimeConstraint constraint, boolean negate) {
        applyIsAfterConstraint(fieldSpec, constraint.referenceValue, true, negate);
    }

    private void applyIsAfterConstraint(FieldSpec fieldSpec, LocalDateTime limit, boolean inclusive, boolean negate) {
        DateTimeRestrictions dateTimeRestrictions = fieldSpec.getDateTimeRestrictions();
        if (dateTimeRestrictions == null) {
            dateTimeRestrictions = new DateTimeRestrictions();
            fieldSpec.setDateTimeRestrictions(dateTimeRestrictions);
        }
        if (negate) {
            dateTimeRestrictions.max = new DateTimeRestrictions.DateTimeLimit(limit, !inclusive);
        }
        else {
            dateTimeRestrictions.min = new DateTimeRestrictions.DateTimeLimit(limit, inclusive);
        }
    }

    private void apply(FieldSpec fieldSpec, IsBeforeConstantDateTimeConstraint constraint, boolean negate) {
        applyIsBeforeConstraint(fieldSpec, constraint.referenceValue, false, negate);
    }

    private void apply(FieldSpec fieldSpec, IsBeforeOrEqualToConstantDateTimeConstraint constraint, boolean negate) {
        applyIsBeforeConstraint(fieldSpec, constraint.referenceValue, true, negate);
    }

    private void applyIsBeforeConstraint(FieldSpec fieldSpec, LocalDateTime limit, boolean inclusive, boolean negate) {
        DateTimeRestrictions dateTimeRestrictions = fieldSpec.getDateTimeRestrictions();
        if (dateTimeRestrictions == null) {
            dateTimeRestrictions = new DateTimeRestrictions();
            fieldSpec.setDateTimeRestrictions(dateTimeRestrictions);
        }
        if (negate) {
            dateTimeRestrictions.min = new DateTimeRestrictions.DateTimeLimit(limit, !inclusive);
        }
        else {
            dateTimeRestrictions.max = new DateTimeRestrictions.DateTimeLimit(limit, inclusive);
        }
    }

    private void apply(FieldSpec fieldSpec, MatchesRegexConstraint constraint, boolean negate) {
        StringRestrictions stringRestrictions = fieldSpec.getStringRestrictions();
        if (stringRestrictions == null) {
            stringRestrictions = new StringRestrictions();
            fieldSpec.setStringRestrictions(stringRestrictions);
        }
        final Automaton nominalAutomaton = automatonFactory.fromPattern(constraint.regex);
        final Automaton automaton = negate
                ? nominalAutomaton.complement()
                : nominalAutomaton;
        stringRestrictions.automaton = automaton;
    }

    private BigDecimal numberToBigDecimal(Number number) {
        if (number instanceof Long) {
            return BigDecimal.valueOf(number.longValue());
        } else if (number instanceof Integer) {
            return BigDecimal.valueOf(number.intValue());
        } else if (number instanceof Double) {
            return BigDecimal.valueOf(number.doubleValue());
        } else if (number instanceof Float) {
            return BigDecimal.valueOf(number.floatValue());
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
