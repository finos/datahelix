package com.scottlogic.deg.generator.decisiontree.testutils;

import java.util.Collections;
import java.util.List;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.MostProlificConstraintOptimiser;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;

public class OptimiseTestStrategy implements TreeTransformationTestStrategy {

    @Override
    public String getTestsDirName() {
        return "optimiser-tests";
    }

    @Override
    public List<DecisionTree> transformTree(DecisionTree beforeTree) {
        DecisionTreeOptimiser treeOptimiser = new MostProlificConstraintOptimiser();
        DecisionTree afterTree = treeOptimiser.optimiseTree(beforeTree);
        return Collections.singletonList(afterTree);
    }
}
