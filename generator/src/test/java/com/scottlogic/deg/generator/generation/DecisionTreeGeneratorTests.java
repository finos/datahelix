package com.scottlogic.deg.generator.generation;

import com.google.inject.name.Named;
import com.scottlogic.deg.common.output.GeneratedObject;
import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.constraints.Constraint;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.TreePartitioner;
import com.scottlogic.deg.generator.generation.combinationstrategies.CombinationStrategy;
import com.scottlogic.deg.generator.generation.databags.DataBag;
import com.scottlogic.deg.generator.validators.ContradictionTreeValidator;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;

public class DecisionTreeGeneratorTests {
    private DecisionTreeDataGenerator generator;
    private DecisionTreeFactory factory;
    private DataGeneratorMonitor monitor;
    private ContradictionTreeValidator treeValidator;
    private TreePartitioner treePartitioner;
    private CombinationStrategy combinationStrategy;
    private DecisionTreeOptimiser optimiser;
    private DecisionTreeWalker treeWalker;
    @BeforeEach
    public void setup() {
        factory = Mockito.mock(DecisionTreeFactory.class);
        treeWalker = Mockito.mock(DecisionTreeWalker.class);
        treePartitioner = Mockito.mock(TreePartitioner.class);
        optimiser = Mockito.mock(DecisionTreeOptimiser.class);
        monitor = Mockito.mock(DataGeneratorMonitor.class);
        treeValidator = Mockito.mock(ContradictionTreeValidator.class);
        combinationStrategy = Mockito.mock(CombinationStrategy.class);
        long maxRows = 10;
        generator = new DecisionTreeDataGenerator(
            factory,
            treeWalker,
            treePartitioner,
            optimiser,
            monitor,
            treeValidator,
            combinationStrategy,
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
            Mockito.when(treeValidator.reportContradictions(tree)).thenReturn(Collections.singleton(rootNode));

            //Act
            Stream<GeneratedObject> actual = generator.generateData(profile);

            //Assert
            assertEquals(0, actual.count());
        }

        @Test
        public void generateData_withWhollyContradictingProfile_reportsContradiction() {
            //Arrange
            Mockito.when(treeValidator.reportContradictions(tree)).thenReturn(Collections.singleton(rootNode));

            //Act
            generator.generateData(profile);

            //Assert
            Mockito.verify(monitor, times(1)).addLineToPrintAtEndOfGeneration(
                eq("The provided profile is wholly contradictory. No fields can successfully be fixed."),
                any()
            );
        }

        @Test
        public void generateData_withProfileWithNoContradictions_canReturnData() {
            //Arrange
            Mockito.when(treeValidator.reportContradictions(tree)).thenReturn(Collections.EMPTY_LIST);

            //Act
            Stream<GeneratedObject> actual = generator.generateData(profile);

            //Assert
            assertNotEquals(0, actual.count());
        }


        @Test
        public void generateData_withPartiallyContradictingProfile_canReturnData() {
            //Arrange
            Mockito.when(treeValidator.reportContradictions(tree)).thenReturn(Collections.singleton(Mockito.mock(Node.class)));

            //Act
            Stream<GeneratedObject> actual = generator.generateData(profile);

            //Assert
            assertNotEquals(0, actual.count());
        }


        @Test
        public void generateData_withPartiallyContradictingProfile_reportsEveryContradiction() {
            //Arrange
            String node0Text = "Node 0";
            String node1Text = "Node 1";
            Node node0 = Mockito.mock(Node.class);
            Node node1 = Mockito.mock(Node.class);
            Mockito.when(node0.toString()).thenReturn(node0Text);
            Mockito.when(node1.toString()).thenReturn(node1Text);
            Mockito.when(treeValidator.reportContradictions(tree)).thenReturn(Arrays.asList(node0, node1));

            //Act
            Stream<GeneratedObject> actual = generator.generateData(profile);

            //Assert
            Mockito.verify(monitor, times(1)).addLineToPrintAtEndOfGeneration(eq(node0Text), any());
            Mockito.verify(monitor, times(1)).addLineToPrintAtEndOfGeneration(eq(node1Text), any());
        }
    }
}
