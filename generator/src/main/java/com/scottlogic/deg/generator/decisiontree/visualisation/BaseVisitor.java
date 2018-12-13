package com.scottlogic.deg.generator.decisiontree.visualisation;

import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.NodeVisitor;

public abstract class BaseVisitor implements NodeVisitor {
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