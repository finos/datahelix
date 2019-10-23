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

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.builders.TestConstraintNodeBuilder;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.ConstraintNodeBuilder;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.NodeMarking;
import com.scottlogic.deg.generator.fieldspecs.*;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.validators.ContradictionDecisionTreeValidator;
import com.scottlogic.deg.generator.walker.pruner.Merged;
import com.scottlogic.deg.generator.walker.pruner.TreePruner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.mockito.Mockito;

import java.util.*;

import static com.scottlogic.deg.generator.builders.TestConstraintNodeBuilder.constraintNode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static com.scottlogic.deg.common.profile.FieldBuilder.createField;

class UpfrontTreePrunerTests {
    private String partialContradictionSubstring = "partially contradictory"; // Implementation Detail
    private String fullContradictionSubstring = "wholly contradictory"; // Implementation Detail

    @Nested
    class unit_tests {
        private DataGeneratorMonitor monitor = Mockito.mock(DataGeneratorMonitor.class);
        private TreePruner treePruner = Mockito.mock(TreePruner.class);
        private ContradictionDecisionTreeValidator contradictionValidator = Mockito.mock(ContradictionDecisionTreeValidator.class);
        private UpfrontTreePruner upfrontTreePruner = new UpfrontTreePruner(treePruner, contradictionValidator);
        private Field fieldA = createField("A");
        private Field fieldB = createField("B");

        @Test
        void runUpfrontPrune_withOneField_returnsPrunedTree() {
            //Arrange
            List<Field> fields = Collections.singletonList(fieldA);
            ConstraintNode prunedRoot = Mockito.mock(ConstraintNode.class);
            Map<Field, FieldSpec> fieldSpecs = new HashMap<>();
            fieldSpecs.put(fieldA, FieldSpecFactory.fromType(fieldA.getType()));

            ConstraintNode unPrunedRoot = Mockito.mock(ConstraintNode.class);
            DecisionTree tree = new DecisionTree(unPrunedRoot, new ProfileFields(fields));
            DecisionTree treeMarkedWithContradictions = new DecisionTree(
                new ConstraintNodeBuilder().build().builder().markNode(NodeMarking.CONTRADICTORY).build(),
                new ProfileFields(fields));

            Mockito.when(treePruner.pruneConstraintNode(unPrunedRoot, fieldSpecs)).thenReturn(Merged.of(prunedRoot));
            Mockito.when(contradictionValidator.markContradictions(tree)).thenReturn(treeMarkedWithContradictions);

            //Act
            DecisionTree actual = upfrontTreePruner.runUpfrontPrune(tree, monitor);

            //Assert
            assertEquals(prunedRoot, actual.getRootNode());
        }

        @Test
        void runUpfrontPrune_withTwoFields_returnsPrunedTree() {
            //Arrange
            List<Field> fields = Arrays.asList(fieldA, fieldB);
            ConstraintNode prunedRoot = Mockito.mock(ConstraintNode.class);
            Map<Field, FieldSpec> fieldSpecs = new HashMap<>();
            fieldSpecs.put(fieldA, FieldSpecFactory.fromType(fieldA.getType()));
            fieldSpecs.put(fieldB, FieldSpecFactory.fromType(fieldB.getType()));

            ConstraintNode unPrunedRoot = Mockito.mock(ConstraintNode.class);
            DecisionTree tree = new DecisionTree(unPrunedRoot, new ProfileFields(fields));

            DecisionTree treeMarkedWithContradictions = new DecisionTree(
                new ConstraintNodeBuilder().build().builder().markNode(NodeMarking.CONTRADICTORY).build(),
                new ProfileFields(fields));

            Mockito.when(treePruner.pruneConstraintNode(unPrunedRoot, fieldSpecs)).thenReturn(Merged.of(prunedRoot));
            Mockito.when(contradictionValidator.markContradictions(tree)).thenReturn(treeMarkedWithContradictions);

            //Act
            DecisionTree actual = upfrontTreePruner.runUpfrontPrune(tree, monitor);

            //Assert
            assertEquals(prunedRoot, actual.getRootNode());
        }

        @Test
        void runUpfrontPrune_whenTreeWhollyContradictory_returnsPrunedTree() {
            //Arrange
            List<Field> fields = Collections.singletonList(fieldA);
            Map<Field, FieldSpec> fieldSpecs = new HashMap<>();
            fieldSpecs.put(fieldA, FieldSpecFactory.fromType(fieldA.getType()));

            ConstraintNode unPrunedRoot = Mockito.mock(ConstraintNode.class);
            DecisionTree tree = new DecisionTree(unPrunedRoot, new ProfileFields(fields));

            //Act
            Mockito.when(treePruner.pruneConstraintNode(unPrunedRoot, fieldSpecs)).thenReturn(Merged.contradictory());

            DecisionTree actual = upfrontTreePruner.runUpfrontPrune(tree, monitor);

            //Assert
            assertNull(actual.getRootNode());
        }

        @Test
        void runUpfrontPrune_whenTreeNotContradictory_reportsNothing() {
            //Arrange
            List<Field> fields = Collections.singletonList(fieldA);
            Map<Field, FieldSpec> fieldSpecs = new HashMap<>();
            fieldSpecs.put(fieldA, FieldSpecFactory.fromType(fieldA.getType()));

            ConstraintNode unPrunedRoot = Mockito.mock(ConstraintNode.class);
            DecisionTree tree = new DecisionTree(unPrunedRoot, new ProfileFields(fields));
            DecisionTree completelyUnmarkedTree = new DecisionTree(
                new ConstraintNodeBuilder().build(),
                new ProfileFields(fields));

            //Act
            Mockito.when(treePruner.pruneConstraintNode(unPrunedRoot, fieldSpecs)).thenReturn(Merged.of(unPrunedRoot));
            Mockito.when(contradictionValidator.markContradictions(tree)).thenReturn(completelyUnmarkedTree);

            upfrontTreePruner.runUpfrontPrune(tree, monitor);

            //Assert
            Mockito.verify(monitor, never()).addLineToPrintAtEndOfGeneration(anyString());
        }

        @Test
        void runUpfrontPrune_whenTreePartiallyContradictory_reportsPartialContradiction() {
            //Arrange
            List<Field> fields = Collections.singletonList(fieldA);
            Map<Field, FieldSpec> fieldSpecs = new HashMap<>();
            fieldSpecs.put(fieldA, FieldSpecFactory.fromType(fieldA.getType()));

            ConstraintNode unPrunedRoot = Mockito.mock(ConstraintNode.class);
            DecisionTree tree = new DecisionTree(unPrunedRoot, new ProfileFields(fields));
            ConstraintNode root = constraintNode()
                .withDecision(
                    constraintNode().markNode(NodeMarking.CONTRADICTORY)).build();
            DecisionTree treeMarkedWithContradictions = new DecisionTree(
                root,
                new ProfileFields(fields));

            //Act
            Mockito.when(treePruner.pruneConstraintNode(unPrunedRoot, fieldSpecs)).thenReturn(Merged.of(root));
            Mockito.when(contradictionValidator.markContradictions(tree)).thenReturn(treeMarkedWithContradictions);

            upfrontTreePruner.runUpfrontPrune(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(partialContradictionSubstring));

        }

        @Test
        void runUpfrontPrune_whenTreeWhollyContradictory_reportsFullContradiction() {
            //Arrange
            List<Field> fields = Collections.singletonList(fieldA);
            Map<Field, FieldSpec> fieldSpecs = new HashMap<>();
            fieldSpecs.put(fieldA, FieldSpecFactory.fromType(fieldA.getType()));

            ConstraintNode unPrunedRoot = Mockito.mock(ConstraintNode.class);
            DecisionTree tree = new DecisionTree(unPrunedRoot, new ProfileFields(fields));
            DecisionTree treeMarkedWithContradictions = new DecisionTree(
                new ConstraintNodeBuilder().build().builder().markNode(NodeMarking.CONTRADICTORY).build(),
                new ProfileFields(fields));

            //Act
            Mockito.when(treePruner.pruneConstraintNode(unPrunedRoot, fieldSpecs)).thenReturn(Merged.contradictory());
            Mockito.when(contradictionValidator.markContradictions(tree)).thenReturn(treeMarkedWithContradictions);

            upfrontTreePruner.runUpfrontPrune(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(fullContradictionSubstring));

        }
    }

    @Nested
    class integration_tests {
        private DataGeneratorMonitor monitor = Mockito.mock(DataGeneratorMonitor.class);
        private ConstraintReducer constraintReducer = new ConstraintReducer(
            new FieldSpecMerger());
        private TreePruner treePruner = new TreePruner(
            new FieldSpecMerger(),
            constraintReducer,
            new FieldSpecHelper());
        private ContradictionDecisionTreeValidator validator = new ContradictionDecisionTreeValidator(
            new RowSpecMerger(
                new FieldSpecMerger()),
            constraintReducer);
        private UpfrontTreePruner upfrontPruner = new UpfrontTreePruner(treePruner, validator);

        @Test
        public void runUpfrontPrune_forNonContradictoryTreeWithOneNode_reportsNoContradictions() {
            //Arrange
            Field fieldA = createField("A");
            Field fieldB = createField("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            ConstraintNode root = constraintNode()
                .where(fieldA).isNull()
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields));

            //Act
            upfrontPruner.runUpfrontPrune(tree, monitor);

            //Assert
            Mockito.verify(monitor, never()).addLineToPrintAtEndOfGeneration(anyString());
        }

        @Test
        public void runUpfrontPrune_forNonContradictoryTreeWithTwoNonContradictoryChildren_reportsNoContradictions() {
            //Arrange
            Field fieldA = createField("A");
            Field fieldB = createField("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            TestConstraintNodeBuilder nonContradictingChild0 = constraintNode().where(fieldA).isNull();
            TestConstraintNodeBuilder nonContradictingChild1 = constraintNode().where(fieldA).isNull();
            ConstraintNode root = constraintNode()
                .withDecision(nonContradictingChild0, nonContradictingChild1)
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields));

            //Act
            upfrontPruner.runUpfrontPrune(tree, monitor);

            //Assert
            Mockito.verify(monitor, never()).addLineToPrintAtEndOfGeneration(anyString());
        }

        @Test
        public void runUpfrontPrune_forNonContradictoryTreeWithContradictionsThatAreNotRelevant_reportsNoContradictions() {
            //Arrange
            Field fieldA = createField("A");
            Field fieldB = createField("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            TestConstraintNodeBuilder fieldAIsNullInThisCase = constraintNode().where(fieldA).isNull();
            TestConstraintNodeBuilder fieldAIsNotNullInThisCase = constraintNode().where(fieldA).isNotNull();
            TestConstraintNodeBuilder nonContradictingChild0 = constraintNode().where(fieldB).isNull();
            TestConstraintNodeBuilder nonContradictingChild1 = constraintNode().where(fieldB).isNull();

            TestConstraintNodeBuilder subTree0 = constraintNode()
                .withDecision(fieldAIsNullInThisCase, nonContradictingChild0);

            TestConstraintNodeBuilder subTree1 = constraintNode()
                .withDecision(fieldAIsNotNullInThisCase, nonContradictingChild1);

            ConstraintNode root = constraintNode()
                .withDecision(subTree0, subTree1)
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields));

            //Act
            upfrontPruner.runUpfrontPrune(tree, monitor);

            //Assert
            Mockito.verify(monitor, never()).addLineToPrintAtEndOfGeneration(anyString());
        }

        @Test
        public void runUpfrontPrune_forPartiallyContradictoryTreeWithTwoContradictionsInDifferentLeaves_reportsNoContradiction() {
            //Arrange
            Field fieldA = createField("A");
            Field fieldB = createField("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);

            ConstraintNode root = constraintNode()
                .where(fieldA).isNull()
                .withDecision(
                    constraintNode()
                        .withDecision(
                            constraintNode()
                                .where(fieldB).isNull(), // Contradicts with one path in other branch
                            constraintNode()
                                .where(fieldA).isNull()),
                    constraintNode()
                        .withDecision(
                            constraintNode()
                                .where(fieldB).isNotNull(), // Contradicts with one path in other branch
                            constraintNode()
                                .where(fieldA).isNull())
                )
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields));

            //Act
            upfrontPruner.runUpfrontPrune(tree, monitor);

            //Assert
            Mockito.verify(monitor, never()).addLineToPrintAtEndOfGeneration(anyString());
        }


        @Test
        public void runUpfrontPrune_forNonContradictoryTreeWithContradictionInOneBranch_reportsPartialContradiction() {
            //Arrange
            Field fieldA = createField("A");
            Field fieldB = createField("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            TestConstraintNodeBuilder contradictingChild = constraintNode().where(fieldA).isNull();
            TestConstraintNodeBuilder nonContradictingChild = constraintNode().where(fieldB).isNull();
            ConstraintNode root = constraintNode()
                .where(fieldA).isNotNull()
                .withDecision(contradictingChild, nonContradictingChild)
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields));

            //Act
            upfrontPruner.runUpfrontPrune(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(partialContradictionSubstring));
        }

        @Test
        public void runUpfrontPrune_forPartiallyContradictoryTreeWithOneContradictoryChild_reportsPartialContradictions() {
            //Arrange
            Field fieldA = createField("A");
            Field fieldB = createField("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            TestConstraintNodeBuilder contradictingChild = constraintNode().where(fieldA).isSelfContradictory();
            TestConstraintNodeBuilder nonContradictingChild = constraintNode().where(fieldA).isNull();
            ConstraintNode root = constraintNode()
                .withDecision(contradictingChild, nonContradictingChild)
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields));

            //Act
            upfrontPruner.runUpfrontPrune(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(partialContradictionSubstring));
        }


        @Test
        public void runUpfrontPrune_forPartiallyContradictoryTreeWithRootContradictingWithOneBranch_reportsPartialContradictions() {
            //Arrange
            Field fieldA = createField("A");
            Field fieldB = createField("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);

            ConstraintNode root = constraintNode()
                .where(fieldA).isNull()
                .withDecision(
                    constraintNode()
                        .where(fieldA).isNotNull(),
                    constraintNode()
                        .withDecision(
                            constraintNode()
                                .where(fieldB).isNull()))
                .build();

            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields));

            //Act
            upfrontPruner.runUpfrontPrune(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(partialContradictionSubstring));
        }



        @Test
        public void runUpfrontPrune_forPartiallyContradictoryTreeWithOneContradictionDeepInBranch_reportsPartialContradictions() {
            //Arrange
            Field fieldA = createField("A");
            Field fieldB = createField("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);

            ConstraintNode root = constraintNode()
                .where(fieldA).isNull()
                .withDecision(
                    constraintNode()
                        .where(fieldB).isNull()
                        .withDecision(
                            constraintNode()
                                .withDecision(
                                    constraintNode()
                                        .where(fieldA).isNotNull())),
                    constraintNode()
                        .withDecision(
                            constraintNode()
                                .where(fieldB).isNull()))
                .build();

            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields));

            //Act
            upfrontPruner.runUpfrontPrune(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(partialContradictionSubstring));
        }

        @Test
        public void runUpfrontPrune_forPartiallyContradictoryTreeWithTwoSelfContradictingLeaves_reportsPartialContradictions() {
            //Arrange
            Field fieldA = createField("A");
            Field fieldB = createField("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);

            ConstraintNode root = constraintNode()
                .where(fieldA).isNull()
                .withDecision(
                    constraintNode()
                        .withDecision(
                            constraintNode()
                                .where(fieldB).isNull()
                                .where(fieldB).isNotNull(),
                            constraintNode()
                                .where(fieldA).isNull()),
                    constraintNode()
                        .withDecision(
                            constraintNode()
                                .where(fieldB).isNull()
                                .where(fieldB).isNotNull(),
                            constraintNode()
                                .where(fieldA).isNull())
                )
                .build();

            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields));

            //Act
            upfrontPruner.runUpfrontPrune(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(partialContradictionSubstring));
        }

        @Test
        public void runUpfrontPrune_forWhollyContradictoryProfileWithOnlyRoot_reportsFullContradiction() {
            //Arrange
            Field fieldA = createField("A");
            Field fieldB = createField("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            ConstraintNode root = constraintNode()
                .where(fieldA).isSelfContradictory()
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields));

            //Act
            upfrontPruner.runUpfrontPrune(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(fullContradictionSubstring));
        }

        @Test
        public void runUpfrontPrune_forWhollyContradictoryProfileWithContradictoryRoot_reportsFullContradiction() {
            //Arrange
            Field fieldA = createField("A");
            Field fieldB = createField("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            ConstraintNode root = constraintNode()
                .where(fieldA).isSelfContradictory()
                .withDecision(
                    constraintNode()
                        .where(fieldB).isNull(),
                    constraintNode()
                        .where(fieldB).isNull())
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields));

            //Act
            upfrontPruner.runUpfrontPrune(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(fullContradictionSubstring));
        }

        @Test
        public void runUpfrontPrune_forWhollyContradictoryProfileWithEveryNodeContradictory_reportsFullContradiction() {
            //Arrange
            Field fieldA = createField("A");
            Field fieldB = createField("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            ConstraintNode root = constraintNode()
                .where(fieldA).isSelfContradictory()
                .withDecision(
                    constraintNode()
                        .where(fieldB).isSelfContradictory(),
                    constraintNode()
                        .where(fieldB).isSelfContradictory(),
                    constraintNode()
                        .where(fieldB).isSelfContradictory())
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields));

            //Act
            upfrontPruner.runUpfrontPrune(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(fullContradictionSubstring));
        }

        @Test
        public void runUpfrontPrune_forWhollyContradictoryProfileWithContradictionDeepInBranch_reportsFullContradiction() {
            //Arrange
            Field fieldA = createField("A");
            Field fieldB = createField("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);

            ConstraintNode root = constraintNode()
                .where(fieldA).isNull()
                .withDecision(
                    constraintNode()
                        .where(fieldB).isNull()
                        .withDecision(
                            constraintNode()
                                .withDecision(
                                    constraintNode()
                                        .where(fieldA).isNotNull())))
                .build();

            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields));

            //Act
            upfrontPruner.runUpfrontPrune(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(fullContradictionSubstring));
        }

        @Test
        public void runUpfrontPrune_forWhollyContradictoryProfileWithAllContradictingNodes_reportsFullContradiction() {
            //Arrange
            Field fieldA = createField("A");
            Field fieldB = createField("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            ConstraintNode root = constraintNode()
                .where(fieldA).isSelfContradictory()
                .withDecision(
                    constraintNode()
                        .where(fieldB).isSelfContradictory(),
                    constraintNode()
                        .where(fieldB).isSelfContradictory(),
                    constraintNode()
                        .where(fieldB).isSelfContradictory())
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields));

            //Act
            upfrontPruner.runUpfrontPrune(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(fullContradictionSubstring));
        }

        @Test
        public void runUpfrontPrune_forWhollyContradictoryProfileWithNonContradictingRoot_reportsFullContradiction() {
            //Arrange
            Field fieldA = createField("A");
            Field fieldB = createField("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            ConstraintNode root = constraintNode()
                .where(fieldA).isNull()
                .withDecision(
                    constraintNode()
                        .where(fieldB).isSelfContradictory(),
                    constraintNode()
                        .where(fieldB).isSelfContradictory(),
                    constraintNode()
                        .where(fieldB).isSelfContradictory())
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields));

            //Act
            upfrontPruner.runUpfrontPrune(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(fullContradictionSubstring));
        }
    }
}
