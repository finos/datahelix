package com.scottlogic.deg.generator.decisiontree.test_utils;

import java.util.List;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;

public interface TreeTransformationTestStrategy {

    String getTestsDirName();

    List<DecisionTree> transformTree(DecisionTree beforeTree);

}
