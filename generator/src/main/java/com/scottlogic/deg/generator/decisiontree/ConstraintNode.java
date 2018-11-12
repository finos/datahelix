package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.restrictions.RowSpec;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface ConstraintNode{
    Collection<IConstraint> getAtomicConstraints();
    Collection<DecisionNode> getDecisions();
    Optional<RowSpec> getOrCreateRowSpec(Supplier<Optional<RowSpec>> createRowSpecFunc);
    void addDecision(DecisionNode decision);
    void removeDecision(DecisionNode decision);
    ConstraintNode cloneWithoutAtomicConstraint(IConstraint excludeAtomicConstraint);
    boolean atomicConstraintExists(IConstraint constraint);
    void addAtomicConstraints(Collection<IConstraint> constraints);
    boolean isOptimised();
    void appendDecisionNode(DecisionNode decisionNode);
}

