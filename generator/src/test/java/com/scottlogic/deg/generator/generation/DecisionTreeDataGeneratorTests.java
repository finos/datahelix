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

package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeFactory;
import com.scottlogic.deg.generator.decisiontree.DecisionTreeOptimiser;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.combinationstrategies.CombinationStrategy;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

public class DecisionTreeDataGeneratorTests {
    private DecisionTreeDataGenerator generator;
    private DecisionTreeFactory factory;
    private DataGeneratorMonitor monitor;
    private TreePartitioner treePartitioner;
    private CombinationStrategy combinationStrategy;
    private DecisionTreeOptimiser optimiser;
    private DecisionTreeWalker treeWalker;
    private UpfrontTreePruner upfrontTreePruner;
    @BeforeEach
    public void setup() {
        factory = Mockito.mock(DecisionTreeFactory.class);
        treeWalker = Mockito.mock(DecisionTreeWalker.class);
        treePartitioner = Mockito.mock(TreePartitioner.class);
        optimiser = Mockito.mock(DecisionTreeOptimiser.class);
        monitor = Mockito.mock(DataGeneratorMonitor.class);
        combinationStrategy = Mockito.mock(CombinationStrategy.class);
        upfrontTreePruner = Mockito.mock(UpfrontTreePruner.class);
        long maxRows = 10;
        generator = new DecisionTreeDataGenerator(
            factory,
            treeWalker,
            treePartitioner,
            optimiser,
            monitor,
            combinationStrategy,
            upfrontTreePruner,
            maxRows
        );
    }

    @Nested
    public class upfrontContradictionChecking {
        private DecisionTree tree;
        private ConstraintNode rootNode;
        private Profile profile;
        @BeforeEach
        public void setup() {
            tree = Mockito.mock(DecisionTree.class);
            rootNode = Mockito.mock(ConstraintNode.class);
            profile = Mockito.mock(Profile.class);
            DataBag value = Mockito.mock(DataBag.class);

            Mockito.when(tree.getRootNode()).thenReturn(rootNode);
            Mockito.when(factory.analyse(profile)).thenReturn(tree);
            Mockito.when(combinationStrategy.permute(any())).thenReturn(Stream.of(value));
            Mockito.when(treePartitioner.splitTreeIntoPartitions(any())).thenReturn(Stream.of(tree));
            Mockito.when(optimiser.optimiseTree(any())).thenReturn(tree);
        }

        @Test
        public void generateData_withWhollyContradictingProfile_returnsEmptyStream() {
            //Arrange
            DecisionTree outputTree = Mockito.mock(DecisionTree.class);
            Mockito.when(outputTree.getRootNode()).thenReturn(null);
            Mockito.when(upfrontTreePruner.runUpfrontPrune(eq(tree), any())).thenReturn(outputTree);

            //Act
            Stream<GeneratedObject> actual = generator.generateData(profile);

            //Assert
            assertEquals(0, actual.count());
        }

        @Test
        public void generateData_withNotWhollyContradictoryProfile_canReturnData() {
            //Arrange
            DecisionTree outputTree = Mockito.mock(DecisionTree.class);
            Mockito.when(outputTree.getRootNode()).thenReturn(rootNode);
            Mockito.when(upfrontTreePruner.runUpfrontPrune(eq(tree), any())).thenReturn(outputTree);

            //Act
            Stream<GeneratedObject> actual = generator.generateData(profile);

            //Assert
            assertNotEquals(0, actual.count());
        }
    }
}
