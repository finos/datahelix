package com.scottlogic.deg.generator.generation.visualiser;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;

public class NoopVisualiser implements Visualiser {

    @Override
    public void printTree(String title, DecisionTree decisionTree) {
        // Does nothing
    }

    @Override
    public void close() {
        // Does nothing
    }
}
