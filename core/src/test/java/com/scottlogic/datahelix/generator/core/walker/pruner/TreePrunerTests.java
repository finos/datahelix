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

package com.scottlogic.datahelix.generator.core.walker.pruner;

import com.scottlogic.datahelix.generator.common.profile.Field;
import com.scottlogic.datahelix.generator.core.decisiontree.ConstraintNode;
import com.scottlogic.datahelix.generator.core.decisiontree.ConstraintNodeBuilder;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpec;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpecFactory;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpecHelper;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpecMerger;
import com.scottlogic.datahelix.generator.core.generation.databags.DataBagValue;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.LongerThanConstraint;
import com.scottlogic.datahelix.generator.core.profile.constraints.atomic.ShorterThanConstraint;
import com.scottlogic.datahelix.generator.core.reducer.ConstraintReducer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.scottlogic.datahelix.generator.common.profile.FieldBuilder.createField;
import static com.scottlogic.datahelix.generator.core.builders.TestConstraintNodeBuilder.constraintNode;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TreePrunerTests {
    private Field field = createField("foo");
    private final FieldSpec notNull = FieldSpecFactory.fromType(field.getType())
        .withNotNull();
    private Field unrelatedField = createField("unrelated");
    private FieldSpecHelper fieldSpecHelper = mock(FieldSpecHelper.class);
    private TreePruner treePruner = new TreePruner(
        new FieldSpecMerger(),
        new ConstraintReducer(
            new FieldSpecMerger()),
        fieldSpecHelper);


    // SINGLE LAYER

    // CONSTRAINT PRUNE:

    // Leaf constraint -> contradicts with last fixed field
    @Test
    public void pruneConstraintNode_leafNodeContradictionsWithParent_returnsContradictory() {
        //Arrange
        Set<Object> inputAllowedList = new HashSet<>(Arrays.asList("a", "b"));
        ConstraintNode tree = new ConstraintNodeBuilder().addAtomicConstraints(new LongerThanConstraint(field, 5)).build();
        FieldSpec inputFieldSpec = FieldSpecFactory.fromAllowedList(inputAllowedList)
            .withNotNull();

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        Merged<ConstraintNode> actual = treePruner.pruneConstraintNode(tree, field, fieldValue());

        //Assert
        Merged<Object> expected = Merged.contradictory();
        assertThat(actual, sameBeanAs(expected));
    }

    // Leaf constraint -> No contradictions with last fixed field
    @Test
    public void pruneConstraintNode_leafNodeNoContradictionsWithParent_returnsLeafNode() {
        //Arrange
        Set<Object> inputAllowedList = new HashSet<>(Arrays.asList("a", "b"));
        ConstraintNode tree = new ConstraintNodeBuilder().addAtomicConstraints(new ShorterThanConstraint(field, 5)).build();
        FieldSpec inputFieldSpec = FieldSpecFactory.fromAllowedList(inputAllowedList);

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        ConstraintNode actual = treePruner.pruneConstraintNode(tree, field, fieldValue()).get();

        //Assert
        ConstraintNode expected = tree;
        assertThat(actual, sameBeanAs(expected));
    }

    // With a decision -> Decision contradicts
    @Test
    public void pruneConstraintNode_withContradictoryDecision_returnsContradictory() {
        //Arrange
        ConstraintNode tree =
            constraintNode()
                .withDecision(
                    constraintNode().where(field).isInSet("a"),
                    constraintNode().where(field).isInSet("b"))
                .build();
        Set<Object> inputAllowedList = new HashSet<>(Arrays.asList("c"));
        FieldSpec inputFieldSpec = FieldSpecFactory.fromAllowedList(inputAllowedList)
            .withNotNull();

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        Merged<ConstraintNode> actual = treePruner.pruneConstraintNode(tree, field, fieldValue());

        //Assert
        Merged<Object> expected = Merged.contradictory();
        assertThat(actual, sameBeanAs(expected));
    }

    // With a decision -> No contradictions, multiple remaining options
    @Test
    public void pruneConstraintNode_withNoContradictoryDecision_returnsSameNode() {
        //Arrange
        ConstraintNode tree =
            constraintNode()
                .withDecision(
                    constraintNode().where(field).isInSet("a"),
                    constraintNode().where(field).isInSet("b"))
                .build();
        Set<Object> inputAllowedList = new HashSet<>(Arrays.asList("a", "b"));
        FieldSpec inputFieldSpec = FieldSpecFactory.fromAllowedList(inputAllowedList).withNotNull();

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        ConstraintNode actual = treePruner.pruneConstraintNode(tree, field, fieldValue()).get();

        //Assert
        ConstraintNode expected = tree;
        assertThat(actual, sameBeanAs(expected));
    }

    // With a decision -> One contradiction, one remaining option
    @Test
    public void pruneConstraintNode_withOneContradictoryDecision_returnConstraintNode() {
        //Arrange
        ConstraintNode tree =
            constraintNode()
                .withDecision(
                    constraintNode().where(field).isInSet("a"),
                    constraintNode().where(field).isInSet("b"))
                .build();
        Set<Object> inputAllowedList = new HashSet<>(Arrays.asList("a"));
        FieldSpec inputFieldSpec = FieldSpecFactory.fromAllowedList(inputAllowedList)
            .withNotNull();

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        ConstraintNode actual = treePruner.pruneConstraintNode(tree, field, fieldValue()).get();

        //Assert
        ConstraintNode expected = constraintNode()
            .where(field).isInSet("a")
            .build();
        assertThat(actual, sameBeanAs(expected));
    }

    // With a decision -> Two or more contradictions, one remaining option
    @Test
    public void pruneConstraintNode_withTwoContradictoryDecisions_returnConstraintNode() {
        //Arrange
        ConstraintNode tree =
            constraintNode()
                .withDecision(
                    constraintNode().where(field).isInSet("a"),
                    constraintNode().where(field).isInSet("b"),
                    constraintNode().where(field).isInSet("c"))
                .build();
        Set<Object> inputAllowedList = new HashSet<>(Arrays.asList("a"));
        FieldSpec inputFieldSpec = FieldSpecFactory.fromAllowedList(inputAllowedList)
            .withNotNull();

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        ConstraintNode actual = treePruner.pruneConstraintNode(tree, field, fieldValue()).get();

        //Assert
        ConstraintNode expected = constraintNode()
            .where(field).isInSet("a")
            .build();
        assertThat(actual, sameBeanAs(expected));
    }

    // With a decision -> One contradiction, Two remaining options
    @Test
    public void pruneConstraintNode_withOneContradictoryDecisionAndMultipleRemainingOptions_returnTreeWithDecision() {
        //Arrange
        ConstraintNode tree =
            constraintNode()
                .withDecision(
                    constraintNode().where(field).isInSet("a"),
                    constraintNode().where(field).isInSet("b"),
                    constraintNode().where(field).isInSet("c"))
                .build();

        Set<Object> inputAllowedList = new HashSet<>(Arrays.asList("a", "b"));
        FieldSpec inputFieldSpec = FieldSpecFactory.fromAllowedList(inputAllowedList)
            .withNotNull();

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        ConstraintNode actual = treePruner.pruneConstraintNode(tree, field, fieldValue()).get();

        //Assert
        ConstraintNode expected = constraintNode()
                .withDecision(
                constraintNode().where(field).isInSet("a"),
                constraintNode().where(field).isInSet("b"))
            .build();

        assertThat(actual, sameBeanAs(expected));
    }

    // Two decisions, one decision is contradictory
    @Test
    public void pruneConstraintNode_twoDecisionsOneContradictory_returnContradictory() {
        //Arrange
        ConstraintNode tree =
            constraintNode()
                .withDecision(
                    constraintNode().where(field).isInSet("valid"),
                    constraintNode().where(unrelatedField).isInSet("unrelated"))
                .withDecision(
                    constraintNode().where(field).isInSet("contradictory"),
                    constraintNode().where(field).isInSet("contradictory"))
                .build();
        Set<Object> inputAllowedList = new HashSet<>(Arrays.asList("valid"));
        FieldSpec inputFieldSpec = FieldSpecFactory.fromAllowedList(inputAllowedList)
            .withNotNull();

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        Merged<ConstraintNode> actual = treePruner.pruneConstraintNode(tree, field, fieldValue());

        //Assert
        Merged<Object> expected = Merged.contradictory();
        assertThat(actual, sameBeanAs(expected));
    }

    // MULTI-LAYERED

    // Each layer has one contradiction -> prunes correctly
    @Test
    public void pruneConstraintNode_MultiLayerOneValidOptionInEach_returnsSingleNode() {
        //Arrange
        ConstraintNode tree =
            constraintNode()
                .withDecision(
                    constraintNode().where(field).isInSet("contradiction"),
                    constraintNode().where(unrelatedField).isInSet("unrelated1")
                        .withDecision(
                            constraintNode().where(field).isInSet("contradiction"),
                            constraintNode().where(unrelatedField).isInSet("unrelated2")
                        ))
                .build();
        Set<Object> inputAllowedList = new HashSet<>(Arrays.asList("valid"));
        FieldSpec inputFieldSpec = FieldSpecFactory.fromAllowedList(inputAllowedList)
            .withNotNull();

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        ConstraintNode actual = treePruner.pruneConstraintNode(tree, field, fieldValue()).get();

        //Assert
        ConstraintNode expected = constraintNode()
            .where(unrelatedField).isInSet("unrelated1")
            .where(unrelatedField).isInSet("unrelated2")
            .build();
        assertEquals(actual.getAtomicConstraints(), expected.getAtomicConstraints());
    }

    // Only one layer has contradiction -> prunes correctly
    @Test
    public void pruneConstraintNode_MultiLayerOneOptionContradicts_returnsSingleLayerTree() {
        //Arrange
        ConstraintNode tree =
            constraintNode()
                .withDecision(
                    constraintNode().where(field).isInSet("contradiction"),
                    constraintNode().where(unrelatedField).isInSet("unrelated1")
                        .withDecision(
                            constraintNode().where(field).isInSet("valid"),
                            constraintNode().where(unrelatedField).isInSet("unrelated2")
                        ))
                .build();
        Set<Object> inputAllowedList = Collections.singleton("valid");
        FieldSpec inputFieldSpec = FieldSpecFactory.fromAllowedList(inputAllowedList)
            .withNotNull();

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        ConstraintNode actual = treePruner.pruneConstraintNode(tree, field, fieldValue()).get();

        //Assert
        ConstraintNode expected = constraintNode()
            .where(unrelatedField).isInSet("unrelated1")
            .withDecision(
                constraintNode().where(field).isInSet("valid"),
                constraintNode().where(unrelatedField).isInSet("unrelated2"))
            .build();
        assertThat(actual, sameBeanAs(expected));
    }

    // Only one layer has contradiction -> prunes correctly
    @Test
    public void pruneConstraintNode_MultiLayerOneOptionContradicts2_returnsSingleLayerTree() {
        //Arrange
        ConstraintNode tree =
            constraintNode()
                .withDecision(
                    constraintNode().where(field).isInSet("valid"),
                    constraintNode().where(unrelatedField).isInSet("unrelated1")
                        .withDecision(
                            constraintNode().where(field).isInSet("contradiction"),
                            constraintNode().where(unrelatedField).isInSet("unrelated2")
                        ))
                .build();
        Set<Object> inputAllowedList = new HashSet<>(Arrays.asList("valid"));
        FieldSpec inputFieldSpec = FieldSpecFactory.fromAllowedList(inputAllowedList)
            .withNotNull();

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        ConstraintNode actual = treePruner.pruneConstraintNode(tree, field, fieldValue()).get();

        //Assert
        ConstraintNode expected = constraintNode()
            .withDecision(
                constraintNode().where(field).isInSet("valid"),
                constraintNode()
                    .where(unrelatedField).isInSet("unrelated1")
                    .where(unrelatedField).isInSet("unrelated2"))
            .build();
        assertEquals(actual.getAtomicConstraints(), expected.getAtomicConstraints());
    }

    // Both layers contradict -> returns invalid tree
    @Test
    public void pruneConstraintNode_MultiLayerMultiContradicts_returnsContradictory() {
        //Arrange
        ConstraintNode tree =
            constraintNode()
                .withDecision(
                    constraintNode().where(field).isInSet("contradiction"),
                    constraintNode().where(unrelatedField).isInSet("unrelated1")
                        .withDecision(
                            constraintNode().where(field).isInSet("contradiction"),
                            constraintNode().where(field).isInSet("contradiction")
                        ))
                .build();
        Set<Object> inputAllowedList = new HashSet<>(Arrays.asList("valid"));
        FieldSpec inputFieldSpec = FieldSpecFactory.fromAllowedList(inputAllowedList)
            .withNotNull();

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        Merged<ConstraintNode> actual = treePruner.pruneConstraintNode(tree, field, fieldValue());

        //Assert
        Merged<Object> expected = Merged.contradictory();
        assertThat(actual, sameBeanAs(expected));
    }

    // No contradictions in any layer -> same tree returned
    @Test
    public void pruneConstraintNode_MultiLayerNoContradicts_returnsOriginalTree() {
        //Arrange
        ConstraintNode tree =
            constraintNode()
                .withDecision(
                    constraintNode().where(field).isInSet("valid"),
                    constraintNode().where(unrelatedField).isInSet("unrelated1")
                        .withDecision(
                            constraintNode().where(field).isInSet("valid"),
                            constraintNode().where(unrelatedField).isInSet("unrelated2")
                        ))
                .build();
        Set<Object> inputAllowedList = new HashSet<>(Arrays.asList("valid"));
        FieldSpec inputFieldSpec = FieldSpecFactory.fromAllowedList(inputAllowedList).withNotNull();

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        ConstraintNode actual = treePruner.pruneConstraintNode(tree, field, fieldValue()).get();

        //Assert
        ConstraintNode expected = tree;
        assertThat(actual, sameBeanAs(expected));
    }

    private DataBagValue fieldValue() {
        return new DataBagValue("TODO");
    }
}
