package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.constraints.IConstraint;

import java.util.Collection;

public interface IRuleOption {
    Collection<IConstraint> getAtomicConstraints();

    Collection<IRuleDecision> getDecisions();
}
