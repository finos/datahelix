package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;

public interface NodeVisitor {
    void visit(ConstraintNode constraintNode);
    void visit(DecisionNode decisionNode);
    void visit(AtomicConstraint atomicConstraint);
}
