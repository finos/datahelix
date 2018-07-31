package com.scottlogic.deg.restriction;

import com.scottlogic.deg.constraint.AmongConstraint;
import com.scottlogic.deg.constraint.IConstraint;
import com.scottlogic.deg.constraint.TypeConstraint;

public class StringRestrictionApplier implements IRestrictionApplier {
    @Override
    public void apply(FieldSpec restriction, IConstraint constraint) {
        if (!(restriction instanceof StringFieldRestriction)) {
            throw new IllegalStateException();
        }
        final var typedRestriction = (StringFieldRestriction) restriction;
        if (constraint instanceof AmongConstraint) {
            typedRestriction.setAmong(((AmongConstraint<String>) constraint).getAmong());
        } else if (constraint instanceof TypeConstraint) {
            // no-op
        } else {
            throw new IllegalStateException();
        }
    }
}
