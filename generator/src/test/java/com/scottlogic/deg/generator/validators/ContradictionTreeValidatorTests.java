package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.Node;
import com.scottlogic.deg.generator.generation.DataGeneratorMonitor;
import com.scottlogic.deg.generator.walker.reductive.Merged;
import com.scottlogic.deg.generator.walker.reductive.ReductiveTreePruner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
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
    private ReductiveTreePruner pruner;
    private DecisionTree mockTree;
    private ConstraintNode rootNode;
    private ConstraintNode child0;
    private ConstraintNode child1;
    private ConstraintNode child2;
    private DataGeneratorMonitor monitor;

    @BeforeEach
    void setup() {
        checker = Mockito.mock(ContradictionChecker.class);
        pruner = Mockito.mock(ReductiveTreePruner.class);
        validator = new ContradictionTreeValidator(checker, pruner);

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
    public void reportThenCullContradictions_withPartiallyContradictingProfile_returnsPrunedTree() {
        //Arrange
        Mockito.when(checker.isContradictory(any(), any())).thenReturn(false);
        Mockito.when(checker.isContradictory(eq(child0), any())).thenReturn(true);

        ConstraintNode prunedNode = Mockito.mock(ConstraintNode.class);
        Merged<ConstraintNode> merged = Merged.of(prunedNode);
        Mockito.when(pruner.pruneConstraintNode(any(), any())).thenReturn(merged);

        ProfileFields profileFields = Mockito.mock(ProfileFields.class);
        Mockito.when(profileFields.getFields()).thenReturn(new ArrayList<>());
        Mockito.when(mockTree.getFields()).thenReturn(profileFields);

        //Act
        DecisionTree checkedTree = validator.reportThenCullContradictions(mockTree, monitor);

        //Assert
        assertEquals(prunedNode, checkedTree.getRootNode());
    }


    @Test
    public void reportThenCullContradictions_withPartiallyContradictingProfile_reportsNumberOfContradictions() {
        //Arrange
        Mockito.when(checker.isContradictory(any(), any())).thenReturn(false);
        Mockito.when(checker.isContradictory(child0, child0)).thenReturn(true);
        Mockito.when(checker.isContradictory(child1, child1)).thenReturn(true);

        ConstraintNode prunedNode = Mockito.mock(ConstraintNode.class);
        Merged<ConstraintNode> merged = Merged.of(prunedNode);
        Mockito.when(pruner.pruneConstraintNode(any(), any())).thenReturn(merged);

        ProfileFields profileFields = Mockito.mock(ProfileFields.class);
        Mockito.when(profileFields.getFields()).thenReturn(new ArrayList<>());
        Mockito.when(mockTree.getFields()).thenReturn(profileFields);

        //Act
        validator.reportThenCullContradictions(mockTree, monitor);

        //Assert
        Mockito.verify(monitor, times(1))
            .addLineToPrintAtEndOfGeneration(
                eq("Warning: There are 2 partial contradiction(s) in the profile." +
                    " Run the profile through the visualiser for more information."),
                any());
    }
}
