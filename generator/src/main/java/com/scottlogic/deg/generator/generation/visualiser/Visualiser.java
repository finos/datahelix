package com.scottlogic.deg.generator.generation.visualiser;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;

public interface Visualiser extends AutoCloseable {
    void printTree(String title, DecisionTree decisionTree);
    @Override
    void close();
}
