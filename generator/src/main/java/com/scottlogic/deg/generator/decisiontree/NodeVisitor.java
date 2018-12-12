package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;

public interface NodeVisitor {
    ConstraintNode visit(ConstraintNode constraintNode);
    DecisionNode visit(DecisionNode decisionNode);
    AtomicConstraint visit(AtomicConstraint atomicConstraint);
}