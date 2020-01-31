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

package com.scottlogic.datahelix.generator.core.generation;

import com.scottlogic.datahelix.generator.common.output.GeneratedObject;
import com.scottlogic.datahelix.generator.core.profile.Profile;
import com.scottlogic.datahelix.generator.core.decisiontree.ConstraintNode;
import com.scottlogic.datahelix.generator.core.decisiontree.DecisionTree;
import com.scottlogic.datahelix.generator.core.decisiontree.DecisionTreeFactory;
import com.scottlogic.datahelix.generator.core.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.datahelix.generator.core.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.datahelix.generator.core.generation.combinationstrategies.CombinationStrategy;
import com.scottlogic.datahelix.generator.core.generation.databags.DataBag;
import com.scottlogic.datahelix.generator.core.generation.visualiser.Visualiser;
import com.scottlogic.datahelix.generator.core.generation.visualiser.VisualiserFactory;
import com.scottlogic.datahelix.generator.core.walker.DecisionTreeWalker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DecisionTreeDataGeneratorTests {
    private DecisionTreeDataGenerator generator;
    private DecisionTreeFactory factory;
    private DataGeneratorMonitor monitor;
    private TreePartitioner treePartitioner;
    private CombinationStrategy combinationStrategy;
    private DecisionTreeOptimiser optimiser;
    private DecisionTreeWalker treeWalker;
    private UpfrontTreePruner upfrontTreePruner;
    private VisualiserFactory visualiserFactory;
    @BeforeEach
    void setup() {
        factory = Mockito.mock(DecisionTreeFactory.class);
        treeWalker = Mockito.mock(DecisionTreeWalker.class);
        treePartitioner = Mockito.mock(TreePartitioner.class);
        optimiser = Mockito.mock(DecisionTreeOptimiser.class);
        monitor = Mockito.mock(DataGeneratorMonitor.class);
        combinationStrategy = Mockito.mock(CombinationStrategy.class);
        upfrontTreePruner = Mockito.mock(UpfrontTreePruner.class);
        visualiserFactory = Mockito.mock(VisualiserFactory.class);
        generator = new DecisionTreeDataGenerator(
            factory,
            treeWalker,
            treePartitioner,
            optimiser,
            monitor,
            combinationStrategy,
            upfrontTreePruner,
            visualiserFactory
        );
    }

    @Nested
    class upfrontContradictionChecking {
        private DecisionTree tree;
        private ConstraintNode rootNode;
        private Profile profile;
        private Visualiser visualiser;
        @BeforeEach
        void setup() throws IOException {
            tree = Mockito.mock(DecisionTree.class);
            rootNode = Mockito.mock(ConstraintNode.class);
            profile = Mockito.mock(Profile.class);
            visualiser = Mockito.mock(Visualiser.class);
            DataBag value = Mockito.mock(DataBag.class);

            Mockito.when(tree.getRootNode()).thenReturn(rootNode);
            Mockito.when(factory.analyse(profile)).thenReturn(tree);
            Mockito.when(combinationStrategy.permute(any())).thenReturn(Stream.of(value));
            Mockito.when(treePartitioner.splitTreeIntoPartitions(any())).thenReturn(Stream.of(tree));
            Mockito.when(optimiser.optimiseTree(any())).thenReturn(tree);
            Mockito.when(visualiserFactory.create(any(), any())).thenReturn(visualiser);
        }

        @Test
        void generateData_withWhollyContradictingProfile_returnsEmptyStream() {
            //Arrange
            DecisionTree outputTree = Mockito.mock(DecisionTree.class);
            Mockito.when(outputTree.getRootNode()).thenReturn(null);
            Mockito.when(upfrontTreePruner.runUpfrontPrune(eq(tree), any())).thenReturn(outputTree);

            //Act
            Stream<GeneratedObject> actual = generator.generateData(profile);

            //Assert
            assertEquals(0, actual.count());

            // Verify
            verify(visualiser, times(2)).printTree(any(), any());
        }

        @Test
        void generateData_withNotWhollyContradictoryProfile_canReturnData() {
            //Arrange
            DecisionTree outputTree = Mockito.mock(DecisionTree.class);
            Mockito.when(outputTree.getRootNode()).thenReturn(rootNode);
            Mockito.when(upfrontTreePruner.runUpfrontPrune(eq(tree), any())).thenReturn(outputTree);

            //Act
            Stream<GeneratedObject> actual = generator.generateData(profile);

            //Assert
            assertNotEquals(0, actual.count());

            // Verify
            verify(visualiser, times(2)).printTree(any(), any());
        }
    }
}
