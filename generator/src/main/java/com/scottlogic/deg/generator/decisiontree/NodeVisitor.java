package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;

public interface NodeVisitor {
    ConstraintNode visit(ConstraintNode constraintNode);
    DecisionNode visit(DecisionNode decisionNode);
    AtomicConstraint visit(AtomicConstraint atomicConstraint);
}

abstract class BaseVisitor implements NodeVisitor {
    @Override
    public ConstraintNode visit(ConstraintNode constraintNode) {
        return constraintNode;
    }

    @Override
    public DecisionNode visit(DecisionNode decisionNode) {
        return decisionNode;
    }

    @Override
    public AtomicConstraint visit(AtomicConstraint atomicConstraint) {
        return atomicConstraint;
    }
}