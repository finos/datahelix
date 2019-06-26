package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.walker.reductive.Merged;
import com.scottlogic.deg.generator.walker.reductive.ReductiveTreePruner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

class UpfrontTreePrunerTests {
    private UpfrontTreePruner upfrontTreePruner;
    private ReductiveTreePruner reductiveTreePruner;
    @BeforeEach
    void setup() {
        reductiveTreePruner = Mockito.mock(ReductiveTreePruner.class);
        upfrontTreePruner = new UpfrontTreePruner(reductiveTreePruner);
    }

    @Test
    void runUpfrontPrune_returnsPrunedTree() {
        //Arrange
        DecisionTree unprunedTree = Mockito.mock(DecisionTree.class);
        Field field = new Field("A");
        List<Field> fields = Collections.singletonList(field);
        ProfileFields profileFields = Mockito.mock(ProfileFields.class);
        Mockito.when(profileFields.getFields()).thenReturn(fields);
        Mockito.when(unprunedTree.getFields()).thenReturn(profileFields);
        ConstraintNode prunedRoot = Mockito.mock(ConstraintNode.class);
        Map<Field, FieldSpec> fieldSpecs = new HashMap<>();
        fieldSpecs.put(field, FieldSpec.Empty);
        Mockito.when(reductiveTreePruner.pruneConstraintNode(any(), eq(fieldSpecs))).thenReturn(Merged.of(prunedRoot));

        //Act
        DecisionTree actual = upfrontTreePruner.runUpfrontPrune(unprunedTree);

        //Assert
        assertEquals(prunedRoot, actual.getRootNode());
    }
}