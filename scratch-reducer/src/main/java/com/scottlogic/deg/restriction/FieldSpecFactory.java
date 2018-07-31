package com.scottlogic.deg.restriction;

import com.scottlogic.deg.constraint.AmongConstraint;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsInSetConstraint;
import com.scottlogic.deg.generator.constraints.NotConstraint;

public class FieldSpecFactory {
    public FieldSpec construct(String name, IConstraint constraint) {
        final FieldSpec fieldSpec = new FieldSpec(name);
        apply(fieldSpec, constraint);
        return fieldSpec;
    }

    private void apply(FieldSpec fieldSpec, IConstraint constraint) {
        if (constraint instanceof NotConstraint) {
            applyNot(fieldSpec, ((NotConstraint) constraint).negatedConstraint);
        } else if (constraint instanceof IsInSetConstraint) {
            apply(fieldSpec, (IsInSetConstraint) constraint);
        } else {
            throw new UnsupportedOperationException();

        }
    }

    private void applyNot(FieldSpec fieldSpec, IConstraint constraint) {
        if (constraint instanceof NotConstraint) {
//            apply(fieldSpec, ((NotConstraint) constraint).negatedConstraint);
            throw new IllegalStateException();
        } else if (constraint instanceof IsInSetConstraint) {
            applyNot(fieldSpec, (IsInSetConstraint) constraint);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private void apply(FieldSpec fieldSpec, IsInSetConstraint constraint) {
        final SetRestrictions setRestrictions = fieldSpec.getSetRestrictions();
        setRestrictions.whitelist = constraint.legalValues;
    }

    private void applyNot(FieldSpec fieldSpec, IsInSetConstraint constraint) {
        final SetRestrictions setRestrictions = fieldSpec.getSetRestrictions();
        setRestrictions.blacklist = constraint.legalValues;
    }
}
