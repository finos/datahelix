package com.scottlogic.deg.generator.decisiontree;

import java.util.Collection;
public interface DecisionNode {
    Collection<ConstraintNode> getOptions();
    DecisionNode setOptions(Collection<ConstraintNode> options);
}

