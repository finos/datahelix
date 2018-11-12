package com.scottlogic.deg.generator.decisiontree;

import java.util.Collection;
public interface DecisionNode {
    Collection<ConstraintNode> getOptions();
    void addOption(ConstraintNode newConstraint);
}

