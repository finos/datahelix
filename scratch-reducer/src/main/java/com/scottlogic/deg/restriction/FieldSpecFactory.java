package com.scottlogic.deg.restriction;

import com.scottlogic.deg.generator.constraints.*;

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
            final IsEqualToConstantConstraint castConstraint = (IsEqualToConstantConstraint) constraint;
            apply(
                    fieldSpec,
                    new IsInSetConstraint(
                            castConstraint.field,
                            Collections.singleton(castConstraint.requiredValue)
                    ),
                    negate
            );
        } else if (constraint instanceof IsNullConstraint) {
            apply(fieldSpec, (IsNullConstraint) constraint, negate);
        } else {
            throw new UnsupportedOperationException();
        }
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
}
