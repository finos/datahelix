package com.scottlogic.deg.generator.decisiontree;

public interface NodeVisitor {
    ConstraintNode visit(ConstraintNode constraintNode);
    DecisionNode visit(DecisionNode decisionNode);
}