package com.scottlogic.deg.generator.profile.constraints.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.HelixTime;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;

public class IsBeforeOrEqualToConstantTimeConstraint implements AtomicConstraint {
    public final Field field;
    public final HelixTime referenceValue;

    public IsBeforeOrEqualToConstantTimeConstraint(Field field, HelixTime referenceValue) {
        this.field = field;
        this.referenceValue = referenceValue;
    }

    @Override
    public Field getField() {
        return null;
    }

    @Override
    public AtomicConstraint negate() {
        return null;
    }

    @Override
    public FieldSpec toFieldSpec() {
        return null;
    }
}
