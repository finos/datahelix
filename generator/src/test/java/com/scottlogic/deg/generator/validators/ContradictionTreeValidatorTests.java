package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.Node;
import com.scottlogic.deg.generator.generation.DataGeneratorMonitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;

public class ContradictionTreeValidatorTests {

    private ContradictionTreeValidator validator;
    private ContradictionChecker checker;
    private DecisionTree mockTree;
    private ConstraintNode rootNode;
    private ConstraintNode child0;
    private ConstraintNode child1;
    private ConstraintNode child2;
    private DataGeneratorMonitor monitor;

    @BeforeEach
    void setup() {
        checker = Mockito.mock(ContradictionChecker.class);
        validator = new ContradictionTreeValidator(checker);

        mockTree = Mockito.mock(DecisionTree.class);
        rootNode = Mockito.mock(ConstraintNode.class);
        DecisionNode subtree0 = Mockito.mock(DecisionNode.class);
        DecisionNode subtree1 = Mockito.mock(DecisionNode.class);
        DecisionNode subtree2 = Mockito.mock(DecisionNode.class);
        child0 = Mockito.mock(ConstraintNode.class);
        child1 = Mockito.mock(ConstraintNode.class);
        child2 = Mockito.mock(ConstraintNode.class);

        Mockito.when(mockTree.getRootNode()).thenReturn(rootNode);
        Mockito.when(rootNode.getDecisions()).thenReturn(Arrays.asList(subtree0, subtree1));
        Mockito.when(subtree0.getOptions()).thenReturn(Collections.singleton(child0));
        Mockito.when(subtree1.getOptions()).thenReturn(Collections.singleton(child1));
        Mockito.when(subtree2.getOptions()).thenReturn(Collections.singleton(child2));

        monitor = Mockito.mock(DataGeneratorMonitor.class);
    }

    @Test
    public void reportContradictions_forNonContradictoryProfile_returnsEmptyCollection() {
        //Arrange
        Mockito.when(checker.isContradictory(any(), any())).thenReturn(false);

        //Act
        Collection<Node> contradictingNodes = validator.reportContradictions(mockTree);

        //Assert
        assertEquals(0, contradictingNodes.size());
    }

    @Test
    public void reportContradictions_forWhollyContradictoryProfile_returnsRootNode() {
        //Arrange
        Mockito.when(checker.isContradictory(any(), any())).thenReturn(false);
        Mockito.when(checker.isContradictory(rootNode, rootNode)).thenReturn(true);

        //Act
        Collection<Node> contradictingNodes = validator.reportContradictions(mockTree);

        //Assert
        assertEquals(1, contradictingNodes.size());
        assertEquals(rootNode, contradictingNodes.stream().findFirst().orElse(null));
    }

    @Test
    public void reportContradictions_forPartiallyContradictoryProfileInFirstSubtree_returnsFirstSubtree() {
        //Arrange
        Mockito.when(checker.isContradictory(any(), any())).thenReturn(false);
        Mockito.when(checker.isContradictory(child0, child0)).thenReturn(true);


        //Act
        Collection<Node> contradictingNodes = validator.reportContradictions(mockTree);

        //Assert
        assertEquals(1, contradictingNodes.size());
        assertEquals(child0, contradictingNodes.stream().findFirst().orElse(null));
    }


    @Test
    public void reportContradictions_forPartiallyContradictoryProfileInSecondSubtree_returnsSecondSubtree() {
        //Arrange
        Mockito.when(checker.isContradictory(any(), any())).thenReturn(false);
        Mockito.when(checker.isContradictory(child1, child1)).thenReturn(true);

        //Act
        Collection<Node> contradictingNodes = validator.reportContradictions(mockTree);

        //Assert
        assertEquals(1, contradictingNodes.size());
        assertEquals(child1, contradictingNodes.stream().findFirst().orElse(null));
    }

    @Test
    public void reportContradictions_forMultiplePartialContradictions_returnsAllContradictions() {
        //Arrange
        DecisionTree mockTree = Mockito.mock(DecisionTree.class);
        ConstraintNode rootNode = Mockito.mock(ConstraintNode.class);
        DecisionNode subtree0 = Mockito.mock(DecisionNode.class);
        DecisionNode subtree1 = Mockito.mock(DecisionNode.class);
        DecisionNode subtree2 = Mockito.mock(DecisionNode.class);
        ConstraintNode child0 = Mockito.mock(ConstraintNode.class);
        ConstraintNode child1 = Mockito.mock(ConstraintNode.class);
        ConstraintNode child2 = Mockito.mock(ConstraintNode.class);

        Mockito.when(mockTree.getRootNode()).thenReturn(rootNode);
        Mockito.when(rootNode.getDecisions()).thenReturn(Arrays.asList(subtree0, subtree1));
        Mockito.when(subtree0.getOptions()).thenReturn(Collections.singleton(child0));
        Mockito.when(subtree1.getOptions()).thenReturn(Collections.singleton(child1));
        Mockito.when(subtree2.getOptions()).thenReturn(Collections.singleton(child2));

        Mockito.when(checker.isContradictory(any(), any())).thenReturn(false);
        Mockito.when(checker.isContradictory(child0, child0)).thenReturn(true);
        Mockito.when(checker.isContradictory(child1, child1)).thenReturn(true);

        //Act
        Collection<Node> contradictingNodes = validator.reportContradictions(mockTree);

        //Assert
        assertEquals(2, contradictingNodes.size());
        assertTrue(contradictingNodes.contains(child0));
        assertTrue(contradictingNodes.contains(child1));
    }

    @Test
    public void reportThenCullContradictions_withWhollyContradictingProfile_returnsTreeWithNullRootNode() {
        //Arrange
        Mockito.when(checker.isContradictory(any(), any())).thenReturn(false);
        Mockito.when(checker.isContradictory(rootNode, rootNode)).thenReturn(true);

        //Act
        DecisionTree checkedTree = validator.reportThenCullContradictions(mockTree, monitor);

        //Assert
        assertNull(checkedTree.getRootNode());
    }

    @Test
    public void reportThenCullContradictions_withWhollyContradictingProfile_reportsContradiction() {
        //Arrange
        Mockito.when(checker.isContradictory(any(), any())).thenReturn(false);
        Mockito.when(checker.isContradictory(rootNode, rootNode)).thenReturn(true);

        //Act
        validator.reportThenCullContradictions(mockTree, monitor);

        //Assert
        Mockito.verify(monitor, times(1)).addLineToPrintAtEndOfGeneration(
            eq("The provided profile is wholly contradictory. No fields can successfully be fixed."),
            any()
        );
    }

    @Test
    public void reportThenCullContradictions_withProfileWithNoContradictions_returnsOriginalTree() {
        //Arrange
        Mockito.when(checker.isContradictory(any(), any())).thenReturn(false);

        //Act
        DecisionTree checkedTree = validator.reportThenCullContradictions(mockTree, monitor);

        //Assert
        assertEquals(mockTree, checkedTree);
    }


    @Test
    public void reportThenCullContradictions_withPartiallyContradictingProfile_returnsOriginalTree() {
        //Arrange
        Mockito.when(checker.isContradictory(any(), any())).thenReturn(false);
        Mockito.when(checker.isContradictory(eq(child0), any())).thenReturn(true);

        //Act
        DecisionTree checkedTree = validator.reportThenCullContradictions(mockTree, monitor);

        //Assert
        assertEquals(mockTree, checkedTree);
    }


    @Test
    public void reportThenCullContradictions_withPartiallyContradictingProfile_reportsEveryContradiction() {
        //Arrange
        String node0Text = "Node 0";
        String node1Text = "Node 1";
        String node2Text = "Node 2";
        Mockito.when(child0.toString()).thenReturn(node0Text);
        Mockito.when(child1.toString()).thenReturn(node1Text);
        Mockito.when(child2.toString()).thenReturn(node2Text);

        Mockito.when(checker.isContradictory(any(), any())).thenReturn(false);
        Mockito.when(checker.isContradictory(child0, child0)).thenReturn(true);
        Mockito.when(checker.isContradictory(child1, child1)).thenReturn(true);

        //Act
        validator.reportThenCullContradictions(mockTree, monitor);

        //Assert
        Mockito.verify(monitor, times(1)).addLineToPrintAtEndOfGeneration(eq(node0Text), any());
        Mockito.verify(monitor, times(1)).addLineToPrintAtEndOfGeneration(eq(node1Text), any());
        Mockito.verify(monitor, times(0)).addLineToPrintAtEndOfGeneration(eq(node2Text), any());
    }
}
