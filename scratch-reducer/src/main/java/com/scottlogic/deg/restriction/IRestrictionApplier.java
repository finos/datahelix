package com.scottlogic.deg.restriction;

import com.scottlogic.deg.constraint.IConstraint;

public interface IRestrictionApplier {
    public void apply(FieldSpec restriction, IConstraint constraint);
}
