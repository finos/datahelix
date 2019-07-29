package com.scottlogic.deg.common.profile.constraints.delayed;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;

public class IsAfterDynamicDateTimeConstraint implements DelayedAtomicConstraint {

    @Override
    public AtomicConstraint underlyingConstraint() {
        return null;
    }

    @Override
    public Field field() {
        return null;
    }

    @Override
    public Constraint negate() {
        return null;
    }
}
