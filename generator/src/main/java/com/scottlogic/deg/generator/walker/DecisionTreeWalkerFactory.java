package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;

public interface DecisionTreeWalkerFactory {
    DecisionTreeWalker getDecisionTreeWalker(DecisionTree tree);
}

