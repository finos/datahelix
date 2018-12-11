package com.scottlogic.deg.generator.decisiontree;

import java.util.Collection;
public interface DecisionNode extends Node {
    Collection<ConstraintNode> getOptions();
    DecisionNode setOptions(Collection<ConstraintNode> options);
    DecisionNode markNode(NodeMarking marking);
    DecisionNode accept(NodeVisitor visitor);
}

