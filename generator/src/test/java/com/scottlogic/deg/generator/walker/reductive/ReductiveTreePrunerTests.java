package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.generation.rows.Value;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.IsLessThanConstantConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.*;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.NullRestrictions;
import com.scottlogic.deg.generator.restrictions.Nullness;
import com.scottlogic.deg.generator.restrictions.SetRestrictions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.scottlogic.deg.generator.builders.ConstraintNodeBuilder.*;
import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReductiveTreePrunerTests {

    public static final FieldSpec notNull = FieldSpec.Empty
        .withNullRestrictions(new NullRestrictions(Nullness.MUST_NOT_BE_NULL), FieldSpecSource.Empty);
    Field field = new Field("foo");
    Field unrelatedField = new Field("unrelated");
    FieldSpecHelper fieldSpecHelper = mock(FieldSpecHelper.class);
    ReductiveTreePruner treePruner = new ReductiveTreePruner(new FieldSpecMerger(), new ConstraintReducer(new FieldSpecFactory(new FieldSpecMerger()), new FieldSpecMerger()), fieldSpecHelper);


    // SINGLE LAYER

    // CONSTRAINT PRUNE:

    // Leaf constraint -> contradicts with last fixed field
    @Test
    public void pruneConstraintNode_leafNodeContradictionsWithParent_returnsContradictory() {
        //Arrange
        Set<Object> inputWhitelist = new HashSet<>(Arrays.asList(10, 20));
        ConstraintNode tree = new TreeConstraintNode(new IsLessThanConstantConstraint(field, 5, Collections.emptySet()));
        FieldSpec inputFieldSpec = notNull.withSetRestrictions(new SetRestrictions(inputWhitelist, Collections.emptySet()), FieldSpecSource.Empty);

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        Merged<ConstraintNode> actual = treePruner.pruneConstraintNode(tree, new Value(field, "TODO"));

        //Assert
        Merged<Object> expected = Merged.contradictory();
        assertThat(actual, sameBeanAs(expected));
    }

    // Leaf constraint -> No contradictions with last fixed field
    @Test
    public void pruneConstraintNode_leafNodeNoContradictionsWithParent_returnsLeafNode() {
        //Arrange
        Set<Object> inputWhitelist = new HashSet<>(Arrays.asList(1, 2));
        ConstraintNode tree = new TreeConstraintNode(new IsLessThanConstantConstraint(field, 5, Collections.emptySet()));
        FieldSpec inputFieldSpec = FieldSpec.Empty.withSetRestrictions(new SetRestrictions(inputWhitelist, Collections.emptySet()), FieldSpecSource.Empty);

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        ConstraintNode actual = treePruner.pruneConstraintNode(tree, new Value(field, "TODO")).get();

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
        Set<Object> inputWhitelist = new HashSet<>(Arrays.asList("c"));
        FieldSpec inputFieldSpec = notNull.withSetRestrictions(new SetRestrictions(inputWhitelist, Collections.emptySet()), FieldSpecSource.Empty);

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        Merged<ConstraintNode> actual = treePruner.pruneConstraintNode(tree, new Value(field, "TODO"));

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
        Set<Object> inputWhitelist = new HashSet<>(Arrays.asList("a", "b"));
        FieldSpec inputFieldSpec = notNull.withSetRestrictions(new SetRestrictions(inputWhitelist, Collections.emptySet()), FieldSpecSource.Empty);

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        ConstraintNode actual = treePruner.pruneConstraintNode(tree, new Value(field, "TODO")).get();

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
        Set<Object> inputWhitelist = new HashSet<>(Arrays.asList("a"));
        FieldSpec inputFieldSpec = notNull.withSetRestrictions(new SetRestrictions(inputWhitelist, Collections.emptySet()), FieldSpecSource.Empty);

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        ConstraintNode actual = treePruner.pruneConstraintNode(tree, new Value(field, "TODO")).get();

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
        Set<Object> inputWhitelist = new HashSet<>(Arrays.asList("a"));
        FieldSpec inputFieldSpec = notNull.withSetRestrictions(new SetRestrictions(inputWhitelist, Collections.emptySet()), FieldSpecSource.Empty);

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        ConstraintNode actual = treePruner.pruneConstraintNode(tree, new Value(field, "TODO")).get();

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

        Set<Object> inputWhitelist = new HashSet<>(Arrays.asList("a", "b"));
        FieldSpec inputFieldSpec = notNull.withSetRestrictions(new SetRestrictions(inputWhitelist, Collections.emptySet()), FieldSpecSource.Empty);

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        ConstraintNode actual = treePruner.pruneConstraintNode(tree, new Value(field, "TODO")).get();

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
        Set<Object> inputWhitelist = new HashSet<>(Arrays.asList("valid"));
        FieldSpec inputFieldSpec = notNull.withSetRestrictions(new SetRestrictions(inputWhitelist, Collections.emptySet()), FieldSpecSource.Empty);

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        Merged<ConstraintNode> actual = treePruner.pruneConstraintNode(tree, new Value(field, "TODO"));

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
        Set<Object> inputWhitelist = new HashSet<>(Arrays.asList("valid"));
        FieldSpec inputFieldSpec = notNull.withSetRestrictions(new SetRestrictions(inputWhitelist, Collections.emptySet()), FieldSpecSource.Empty);

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        ConstraintNode actual = treePruner.pruneConstraintNode(tree, new Value(field, "TODO")).get();

        //Assert
        ConstraintNode expected = constraintNode()
            .where(unrelatedField).isInSet("unrelated1")
            .where(unrelatedField).isInSet("unrelated2")
            .build();
        assertThat(actual, sameBeanAs(expected));
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
        Set<Object> inputWhitelist = new HashSet<>(Arrays.asList("valid"));
        FieldSpec inputFieldSpec = notNull.withSetRestrictions(new SetRestrictions(inputWhitelist, Collections.emptySet()), FieldSpecSource.Empty);

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        ConstraintNode actual = treePruner.pruneConstraintNode(tree, new Value(field, "TODO")).get();

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
        Set<Object> inputWhitelist = new HashSet<>(Arrays.asList("valid"));
        FieldSpec inputFieldSpec = notNull.withSetRestrictions(new SetRestrictions(inputWhitelist, Collections.emptySet()), FieldSpecSource.Empty);

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        ConstraintNode actual = treePruner.pruneConstraintNode(tree, new Value(field, "TODO")).get();

        //Assert
        ConstraintNode expected = constraintNode()
            .withDecision(
                constraintNode().where(field).isInSet("valid"),
                constraintNode()
                    .where(unrelatedField).isInSet("unrelated1")
                    .where(unrelatedField).isInSet("unrelated2"))
            .build();
        assertThat(actual, sameBeanAs(expected));
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
        Set<Object> inputWhitelist = new HashSet<>(Arrays.asList("valid"));
        FieldSpec inputFieldSpec = notNull.withSetRestrictions(new SetRestrictions(inputWhitelist, Collections.emptySet()), FieldSpecSource.Empty);

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        Merged<ConstraintNode> actual = treePruner.pruneConstraintNode(tree, new Value(field, "TODO"));

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
        Set<Object> inputWhitelist = new HashSet<>(Arrays.asList("valid"));
        FieldSpec inputFieldSpec = notNull.withSetRestrictions(new SetRestrictions(inputWhitelist, Collections.emptySet()), FieldSpecSource.Empty);

        when(fieldSpecHelper.getFieldSpecForValue(any())).thenReturn(inputFieldSpec);

        //Act
        ConstraintNode actual = treePruner.pruneConstraintNode(tree, new Value(field, "TODO")).get();

        //Assert
        ConstraintNode expected = tree;
        assertThat(actual, sameBeanAs(expected));
    }
}