package com.scottlogic.deg.restriction;

import com.scottlogic.deg.generator.constraints.*;

import java.math.BigDecimal;
import java.util.Collections;

public class FieldSpecFactory {
    public FieldSpec construct(String name, IConstraint constraint) {
        final FieldSpec fieldSpec = new FieldSpec(name);
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
        } else if (constraint instanceof IsNullConstraint) {
            apply(fieldSpec, (IsNullConstraint) constraint, negate);
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
        final SetRestrictions setRestrictions = fieldSpec.getSetRestrictions();
        if (negate) {
            setRestrictions.blacklist = constraint.legalValues;
        } else {
            setRestrictions.whitelist = constraint.legalValues;
        }
    }

    private void apply(FieldSpec fieldSpec, IsNullConstraint constraint, boolean negate) {
        final NullRestrictions nullRestrictions = fieldSpec.getNullRestrictions();
        nullRestrictions.nullness = negate
                ? NullRestrictions.Nullness.MustNotBeNull
                : NullRestrictions.Nullness.MustBeNull;
    }

    private void apply(FieldSpec fieldSpec, IsGreaterThanConstantConstraint constraint, boolean negate) {
        final NumericRestrictions numericRestrictions = fieldSpec.getNumericRestrictions();
        final BigDecimal limit = numberToBigDecimal(constraint.referenceValue);
        if (negate) {
            numericRestrictions.max = new NumericRestrictions.NumericLimit(
                    limit,
                    true
            );
        } else {
            numericRestrictions.min = new NumericRestrictions.NumericLimit(
                    limit,
                    false
            );
        }
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
