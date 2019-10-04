package com.scottlogic.deg.generator.generation.visualiser;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.visualisation.DecisionTreeVisualisationWriter;

import java.io.IOException;

public class DotVisualiser implements Visualiser {

    private final DecisionTreeVisualisationWriter decisionTreeVisualisationWriter;

    DotVisualiser(DecisionTreeVisualisationWriter decisionTreeVisualisationWriter) {
        this.decisionTreeVisualisationWriter = decisionTreeVisualisationWriter;
    }

    @Override
    public void printTree(String title, DecisionTree decisionTree) {
        try {
            System.err.println("VISUALISING decision tree with title: " + title);
            decisionTreeVisualisationWriter.writeDot(decisionTree, title, title);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        decisionTreeVisualisationWriter.close();
    }
}
