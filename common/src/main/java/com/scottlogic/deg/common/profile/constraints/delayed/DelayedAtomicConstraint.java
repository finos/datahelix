package com.scottlogic.deg.common.profile.constraints.delayed;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;

public interface DelayedAtomicConstraint extends Constraint {

    AtomicConstraint underlyingConstraint();

    Field field();

    default DynamicNotConstraint negate() {
        return new DynamicNotConstraint(this);
    }

}
