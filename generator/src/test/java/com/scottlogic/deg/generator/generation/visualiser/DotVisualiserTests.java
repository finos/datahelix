/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scottlogic.deg.generator.generation.visualiser;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.visualisation.DecisionTreeVisualisationWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class DotVisualiserTests {
    private DotVisualiser dotVisualiser;
    private DecisionTreeVisualisationWriter decisionTreeVisualisationWriter;

    @BeforeEach
    void setUp() {
        decisionTreeVisualisationWriter = mock(DecisionTreeVisualisationWriter.class);
        dotVisualiser = new DotVisualiser(decisionTreeVisualisationWriter);
    }

    @Test
    void printTree() throws IOException {
        DecisionTree decisionTree = mock(DecisionTree.class);
        String title = "title";
        dotVisualiser.printTree(title, decisionTree);
        verify(decisionTreeVisualisationWriter, times(1)).writeDot(decisionTree, title, title);
        verify(decisionTreeVisualisationWriter, times(1)).writeDot(any(), any(), any());
        verify(decisionTreeVisualisationWriter, times(0)).close();
    }

    @Test
    void close() throws IOException {
        dotVisualiser.close();
        verify(decisionTreeVisualisationWriter, times(0)).writeDot(any(), any(), any());
        verify(decisionTreeVisualisationWriter, times(1)).close();
    }
}
