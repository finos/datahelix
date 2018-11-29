package com.scottlogic.deg.generator.decisiontree.test_utils;

import java.util.Collections;
import java.util.List;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.IDecisionTreeOptimiser;

public class OptimiseTestStrategy implements TreeTransformationTestStrategy {

    @Override
    public String getTestsDirName() {
        return "optimiser-tests";
    }

    @Override
    public List<DecisionTree> transformTree(DecisionTree beforeTree) {
        IDecisionTreeOptimiser treeOptimiser = new DecisionTreeOptimiser();            
        DecisionTree afterTree = treeOptimiser.optimiseTree(beforeTree);
        return Collections.singletonList(afterTree);
    }
}
