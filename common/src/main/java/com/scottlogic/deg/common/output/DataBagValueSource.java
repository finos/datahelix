package com.scottlogic.deg.common.output;

import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;

import java.util.Collections;
import java.util.Set;

public class DataBagValueSource {
    public static final DataBagValueSource Empty =
        new DataBagValueSource(Collections.emptySet(), Collections.emptySet());

    private final Set<AtomicConstraint> constraints;
    private final Set<AtomicConstraint> violatedConstraints;

    public DataBagValueSource(
        Set<AtomicConstraint> constraints,
        Set<AtomicConstraint> violatedConstraints)
    {
        this.constraints = constraints;
        this.violatedConstraints = violatedConstraints;
    }

    public Set<AtomicConstraint> getConstraints() {
        return constraints;
    }

    public Set<AtomicConstraint> getViolatedConstraints() {
        return violatedConstraints;
    }
}
