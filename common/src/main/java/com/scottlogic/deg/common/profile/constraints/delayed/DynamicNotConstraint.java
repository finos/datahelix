package com.scottlogic.deg.common.profile.constraints.delayed;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;

import java.util.Objects;

public class DynamicNotConstraint implements DelayedAtomicConstraint {

    private final DelayedAtomicConstraint negatedConstraint;

    public DynamicNotConstraint(DelayedAtomicConstraint negatedConstraint) {
        if (negatedConstraint instanceof DynamicNotConstraint) {
            throw new IllegalArgumentException("Nested DynamicNotConstraint not allowed");
        }
        this.negatedConstraint = negatedConstraint;
    }

    @Override
    public AtomicConstraint underlyingConstraint() {
        return negatedConstraint.underlyingConstraint();
    }

    @Override
    public Field field() {
        return negatedConstraint.field();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamicNotConstraint that = (DynamicNotConstraint) o;
        return Objects.equals(negatedConstraint, that.negatedConstraint);
    }

    @Override
    public int hashCode() {
        return Objects.hash("NOT", negatedConstraint);
    }
}
