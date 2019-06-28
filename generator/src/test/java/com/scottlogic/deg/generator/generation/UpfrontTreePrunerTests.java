package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.walker.reductive.Merged;
import com.scottlogic.deg.generator.walker.reductive.ReductiveTreePruner;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

class UpfrontTreePrunerTests {
    private ReductiveTreePruner reductiveTreePruner = Mockito.mock(ReductiveTreePruner.class);
    private UpfrontTreePruner upfrontTreePruner = new UpfrontTreePruner(reductiveTreePruner);
    private Field fieldA = new Field("A");
    private Field fieldB = new Field("B");

    @Test
    void runUpfrontPrune_withOneField_returnsPrunedTree() {
        //Arrange
        List<Field> fields = Collections.singletonList(fieldA);
        ConstraintNode prunedRoot = Mockito.mock(ConstraintNode.class);
        Map<Field, FieldSpec> fieldSpecs = new HashMap<>();
        fieldSpecs.put(fieldA, FieldSpec.Empty);

        ConstraintNode unPrunedRoot = Mockito.mock(ConstraintNode.class);
        DecisionTree tree = new DecisionTree(unPrunedRoot, new ProfileFields(fields));

        Mockito.when(reductiveTreePruner.pruneConstraintNode(unPrunedRoot, fieldSpecs)).thenReturn(Merged.of(prunedRoot));

        //Act
        DecisionTree actual = upfrontTreePruner.runUpfrontPrune(tree);

        //Assert
        assertEquals(prunedRoot, actual.getRootNode());
    }

    @Test
    void runUpfrontPrune_withTwoFields_returnsPrunedTree() {
        //Arrange
        List<Field> fields = Arrays.asList(fieldA, fieldB);
        ConstraintNode prunedRoot = Mockito.mock(ConstraintNode.class);
        Map<Field, FieldSpec> fieldSpecs = new HashMap<>();
        fieldSpecs.put(fieldA, FieldSpec.Empty);
        fieldSpecs.put(fieldB, FieldSpec.Empty);

        ConstraintNode unPrunedRoot = Mockito.mock(ConstraintNode.class);
        DecisionTree tree = new DecisionTree(unPrunedRoot, new ProfileFields(fields));

        Mockito.when(reductiveTreePruner.pruneConstraintNode(unPrunedRoot, fieldSpecs)).thenReturn(Merged.of(prunedRoot));

        //Act
        DecisionTree actual = upfrontTreePruner.runUpfrontPrune(tree);

        //Assert
        assertEquals(prunedRoot, actual.getRootNode());
    }

    @Test
    void runUpfrontPrune_whenTreeWhollyContradictory_returnsPrunedTree() {
        //Arrange
        List<Field> fields = Collections.singletonList(fieldA);
        Map<Field, FieldSpec> fieldSpecs = new HashMap<>();
        fieldSpecs.put(fieldA, FieldSpec.Empty);

        ConstraintNode unPrunedRoot = Mockito.mock(ConstraintNode.class);
        DecisionTree tree = new DecisionTree(unPrunedRoot, new ProfileFields(fields));

        //Act
        Mockito.when(reductiveTreePruner.pruneConstraintNode(unPrunedRoot, fieldSpecs)).thenReturn(Merged.contradictory());

        DecisionTree actual = upfrontTreePruner.runUpfrontPrune(tree);

        //Assert
        assertNull(actual.getRootNode());
    }
}
