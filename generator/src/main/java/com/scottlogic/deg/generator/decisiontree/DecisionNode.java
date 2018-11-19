package com.scottlogic.deg.generator.decisiontree;

import java.util.Collection;
public interface DecisionNode {
    Collection<ConstraintNode> getOptions();
    DecisionNode addOptions(Collection<ConstraintNode> newConstraints);
    DecisionNode setOptions(Collection<ConstraintNode> constraints);
}

