package com.scottlogic.deg.generator.decisiontree;

import java.util.Collection;
public interface DecisionNode extends Node {
    Collection<ConstraintNode> getOptions();
    DecisionNode setOptions(Collection<ConstraintNode> options);
    DecisionNode markNode(NodeMarking marking);
    DecisionNode accept(NodeVisitor visitor);

    @Override
    default Node getFirstChild() {
        return getOptions().stream().findFirst().orElse(null);
    }

    @Override
    default Node getSecondChild() {
        return getOptions().stream().skip(1).findFirst().orElse(null);
    }
}

