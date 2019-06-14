package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;

public class ContradictionTreeValidatorTests {

    private ContradictionTreeValidator validator;
    private ContradictionChecker checker;
    private DecisionTree mockTree;
    private ConstraintNode rootNode;
    private ConstraintNode child0;
    private ConstraintNode child1;

    @BeforeEach
    void setup() {
        checker = Mockito.mock(ContradictionChecker.class);
        validator = new ContradictionTreeValidator(checker);

        mockTree = Mockito.mock(DecisionTree.class);
        rootNode = Mockito.mock(ConstraintNode.class);
        DecisionNode subtree0 = Mockito.mock(DecisionNode.class);
        DecisionNode subtree1 = Mockito.mock(DecisionNode.class);
        child0 = Mockito.mock(ConstraintNode.class);
        child1 = Mockito.mock(ConstraintNode.class);

        Mockito.when(mockTree.getRootNode()).thenReturn(rootNode);
        Mockito.when(rootNode.getDecisions()).thenReturn(Arrays.asList(subtree0, subtree1));
        Mockito.when(subtree0.getOptions()).thenReturn(Collections.singleton(child0));
        Mockito.when(subtree1.getOptions()).thenReturn(Collections.singleton(child1));
    }

    @Test
    public void reportContradictions_forNonContradictoryProfile_returnsEmptyCollection() {
        //Arrange
        Mockito.when(checker.checkContradictions(any(), any())).thenReturn(false);

        //Act
        Collection<Node> contradictingNodes = validator.reportContradictions(mockTree);

        //Assert
        assertEquals(0, contradictingNodes.size());
    }

    @Test
    public void reportContradictions_forWhollyContradictoryProfile_returnsRootNode() {
        //Arrange
        Mockito.when(checker.checkContradictions(any(), any())).thenReturn(false);
        Mockito.when(checker.checkContradictions(rootNode, rootNode)).thenReturn(true);

        //Act
        Collection<Node> contradictingNodes = validator.reportContradictions(mockTree);

        //Assert
        assertEquals(1, contradictingNodes.size());
        assertEquals(rootNode, contradictingNodes.stream().findFirst().orElse(null));
    }

    @Test
    public void reportContradictions_forPartiallyContradictoryProfileInFirstSubtree_returnsFirstSubtree() {
        //Arrange
        Mockito.when(checker.checkContradictions(any(), any())).thenReturn(false);
        Mockito.when(checker.checkContradictions(child0, child0)).thenReturn(true);


        //Act
        Collection<Node> contradictingNodes = validator.reportContradictions(mockTree);

        //Assert
        assertEquals(1, contradictingNodes.size());
        assertEquals(child0, contradictingNodes.stream().findFirst().orElse(null));
    }


    @Test
    public void reportContradictions_forPartiallyContradictoryProfileInSecondSubtree_returnsSecondSubtree() {
        //Arrange
        Mockito.when(checker.checkContradictions(any(), any())).thenReturn(false);
        Mockito.when(checker.checkContradictions(child1, child1)).thenReturn(true);

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

        Mockito.when(checker.checkContradictions(any(), any())).thenReturn(false);
        Mockito.when(checker.checkContradictions(child0, child0)).thenReturn(true);
        Mockito.when(checker.checkContradictions(child1, child1)).thenReturn(true);

        //Act
        Collection<Node> contradictingNodes = validator.reportContradictions(mockTree);

        //Assert
        assertEquals(2, contradictingNodes.size());
        assertTrue(contradictingNodes.contains(child0));
        assertTrue(contradictingNodes.contains(child1));
    }
}
