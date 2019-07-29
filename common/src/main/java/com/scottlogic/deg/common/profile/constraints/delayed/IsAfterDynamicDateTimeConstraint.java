package com.scottlogic.deg.common.profile.constraints.delayed;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;

public class IsAfterDynamicDateTimeConstraint implements DelayedAtomicConstraint {

    private final AtomicConstraint underlyingConstraint;

    private final Field field;

    public IsAfterDynamicDateTimeConstraint(AtomicConstraint underlyingConstraint,
                                            Field field) {
        this.underlyingConstraint = underlyingConstraint;
        this.field = field;
    }

    @Override
    public AtomicConstraint underlyingConstraint() {
        return underlyingConstraint;
    }

    @Override
    public Field field() {
        return field;
    }

}
