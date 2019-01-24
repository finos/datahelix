package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;

import java.util.Objects;

public class AtomicConstraintConstructTuple {
    private final AtomicConstraint atomicConstraint;
    private final boolean matchFullString;
    private final boolean negate;

    AtomicConstraintConstructTuple(AtomicConstraint atomicConstraint, boolean matchFullString, boolean negate) {
        this.atomicConstraint = atomicConstraint;
        this.matchFullString = matchFullString;
        this.negate = negate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AtomicConstraintConstructTuple that = (AtomicConstraintConstructTuple) o;
        return Objects.equals(atomicConstraint, that.atomicConstraint) &&
            Objects.equals(matchFullString, that.matchFullString) &&
            Objects.equals(negate, that.negate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atomicConstraint, matchFullString, negate);
    }
}
