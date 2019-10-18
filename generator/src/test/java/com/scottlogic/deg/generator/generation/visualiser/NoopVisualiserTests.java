package com.scottlogic.deg.generator.generation.visualiser;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class NoopVisualiserTests {
    private NoopVisualiser noopVisualiser;

    @BeforeEach
    void setUp() {
        noopVisualiser = new NoopVisualiser();
    }

    @Test
    void printTree() {
        DecisionTree decisionTree = mock(DecisionTree.class);
        noopVisualiser.printTree("title", decisionTree);
    }

    @Test
    void close() {
        noopVisualiser.close();
    }
}
