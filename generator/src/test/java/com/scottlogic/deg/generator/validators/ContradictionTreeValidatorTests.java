package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.generator.builders.ConstraintNodeBuilder;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecHelper;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.generation.DataGeneratorMonitor;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory;
import com.scottlogic.deg.generator.walker.reductive.Merged;
import com.scottlogic.deg.generator.walker.reductive.ReductiveTreePruner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.scottlogic.deg.generator.builders.ConstraintNodeBuilder.constraintNode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

public class ContradictionTreeValidatorTests {
    @Nested
    class integration_tests {
        private ContradictionTreeValidator validator;
        private ContradictionChecker checker;
        private ReductiveTreePruner pruner;
        private DataGeneratorMonitor monitor;
        private String onePartialContradictionSubstring = "1 partial contradiction"; // Implementation Detail
        private String twoPartialContradictionsSubstring = "2 partial contradiction"; // Implementation Detail
        private String fullContradictionSubstring = "wholly contradictory"; // Implementation Detail

        @BeforeEach
        void setup() {
            checker = new ContradictionChecker(
                new ConstraintReducer(
                    new FieldSpecFactory(
                        new StringRestrictionsFactory()),
                    new FieldSpecMerger()));
            pruner = new ReductiveTreePruner(new FieldSpecMerger(), new ConstraintReducer(new FieldSpecFactory(new StringRestrictionsFactory()), new FieldSpecMerger()), new FieldSpecHelper());
            validator = new ContradictionTreeValidator(checker, pruner);
            monitor = Mockito.mock(DataGeneratorMonitor.class);
        }

        @Test
        public void reportThenCullContradictions_forNonContradictoryTreeWithOneNode_reportsNoContradictions() {
            //Arrange
            Field fieldA = new Field("A");
            Field fieldB = new Field("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            ConstraintNode root = constraintNode()
                .where(fieldA).isNull()
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields), "Description");

            //Act
            validator.reportThenCullContradictions(tree, monitor);

            //Assert
            Mockito.verify(monitor, never()).addLineToPrintAtEndOfGeneration(anyString(), any());
        }

        @Test
        public void reportThenCullContradictions_forNonContradictoryTreeWithTwoNonContradictoryChildren_reportsNoContradictions() {
            //Arrange
            Field fieldA = new Field("A");
            Field fieldB = new Field("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            ConstraintNodeBuilder nonContradictingChild0 = constraintNode().where(fieldA).isNull();
            ConstraintNodeBuilder nonContradictingChild1 = constraintNode().where(fieldA).isNull();
            ConstraintNode root = constraintNode()
                .withDecision(nonContradictingChild0, nonContradictingChild1)
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields), "Description");

            //Act
            DecisionTree updatedTree = validator.reportThenCullContradictions(tree, monitor);

            //Assert
            Mockito.verify(monitor, never()).addLineToPrintAtEndOfGeneration(anyString(), any());
            assertEquals(tree, updatedTree);
        }

        @Test
        public void reportThenCullContradictions_forNonContradictoryTreeWithContradictionsThatAreNotRelevant_reportsNoContradictions() {
            //Arrange
            Field fieldA = new Field("A");
            Field fieldB = new Field("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            ConstraintNodeBuilder fieldAIsNullInThisCase = constraintNode().where(fieldA).isNull();
            ConstraintNodeBuilder fieldAIsNotNullInThisCase = constraintNode().where(fieldA).isNotNull();
            ConstraintNodeBuilder nonContradictingChild0 = constraintNode().where(fieldB).isNull();
            ConstraintNodeBuilder nonContradictingChild1 = constraintNode().where(fieldB).isNull();

            ConstraintNodeBuilder subTree0 = constraintNode()
                .withDecision(fieldAIsNullInThisCase, nonContradictingChild0);

            ConstraintNodeBuilder subTree1 = constraintNode()
                .withDecision(fieldAIsNotNullInThisCase, nonContradictingChild1);

            ConstraintNode root = constraintNode()
                .withDecision(subTree0, subTree1)
                .build();



            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields), "Description");

            //Act
            DecisionTree updatedTree = validator.reportThenCullContradictions(tree, monitor);

            //Assert
            Mockito.verify(monitor, never()).addLineToPrintAtEndOfGeneration(anyString(), any());
            assertEquals(tree, updatedTree);
        }

        @Test
        public void reportThenCullContradictions_forNonContradictoryTreeWithContradictionInOneBranch_reportsNoContradictions() {
            //Arrange
            Field fieldA = new Field("A");
            Field fieldB = new Field("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            ConstraintNodeBuilder contradictingChild = constraintNode().where(fieldA).isNull();
            ConstraintNodeBuilder nonContradictingChild = constraintNode().where(fieldB).isNull();
            ConstraintNode root = constraintNode()
                .where(fieldA).isNotNull()
                .withDecision(contradictingChild, nonContradictingChild)
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields), "Description");

            //Act
            DecisionTree updatedTree = validator.reportThenCullContradictions(tree, monitor);

            //Assert
            Mockito.verify(monitor, never()).addLineToPrintAtEndOfGeneration(anyString(), any());
            assertEquals(tree, updatedTree);
        }

        @Test
        public void reportThenCullContradictions_forPartiallyContradictoryTreeWithTwoContradictionsInDifferentLeaves_reportsNoContradiction() {
            //Arrange
            Field fieldA = new Field("A");
            Field fieldB = new Field("B");
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


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields), "Description");

            //Act
            DecisionTree updatedTree = validator.reportThenCullContradictions(tree, monitor);

            //Assert
            Mockito.verify(monitor, never()).addLineToPrintAtEndOfGeneration(anyString(), any());
            assertEquals(tree, updatedTree);
        }

        @Test
        public void reportThenCullContradictions_forPartiallyContradictoryTreeWithOneContradictoryChild_reportsPartialContradictions() {
            //Arrange
            Field fieldA = new Field("A");
            Field fieldB = new Field("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            ConstraintNodeBuilder contradictingChild = constraintNode().where(fieldA).isContradictory();
            ConstraintNodeBuilder nonContradictingChild = constraintNode().where(fieldA).isNull();
            ConstraintNode root = constraintNode()
                .withDecision(contradictingChild, nonContradictingChild)
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields), "Description");

            //Act
            DecisionTree updatedTree = validator.reportThenCullContradictions(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(onePartialContradictionSubstring), any());
            assertNotEquals(tree, updatedTree);
        }


        @Test
        public void reportThenCullContradictions_forPartiallyContradictoryTreeWithRootContradictingWithOneBranch_reportsPartialContradictions() {
            //Arrange
            Field fieldA = new Field("A");
            Field fieldB = new Field("B");
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

            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields), "Description");

            //Act
            DecisionTree updatedTree = validator.reportThenCullContradictions(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(onePartialContradictionSubstring), any());
            assertNotEquals(tree, updatedTree);
        }



        @Test
        public void reportThenCullContradictions_forPartiallyContradictoryTreeWithOneContradictionDeepInBranch_reportsPartialContradictions() {
            //Arrange
            Field fieldA = new Field("A");
            Field fieldB = new Field("B");
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

            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields), "Description");

            //Act
            DecisionTree updatedTree = validator.reportThenCullContradictions(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(onePartialContradictionSubstring), any());
            assertNotEquals(tree, updatedTree);
        }

        @Test
        public void reportThenCullContradictions_forPartiallyContradictoryTreeWithTwoSelfContradictingLeaves_reportsPartialContradictions() {
            //Arrange
            Field fieldA = new Field("A");
            Field fieldB = new Field("B");
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

            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields), "Description");

            //Act
            DecisionTree updatedTree = validator.reportThenCullContradictions(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(twoPartialContradictionsSubstring), any());
            assertNotEquals(tree, updatedTree);
        }

        @Test
        public void reportThenCullContradictions_forWhollyContradictoryProfileWithOnlyRoot_reportsFullContradiction() {
            //Arrange
            Field fieldA = new Field("A");
            Field fieldB = new Field("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            ConstraintNode root = constraintNode()
                .where(fieldA).isContradictory()
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields), "Description");

            //Act
            DecisionTree updatedTree = validator.reportThenCullContradictions(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(fullContradictionSubstring), any());
            assertNotEquals(tree, updatedTree);
        }

        @Test
        public void reportThenCullContradictions_forWhollyContradictoryProfileWithContradictoryRoot_reportsFullContradiction() {
            //Arrange
            Field fieldA = new Field("A");
            Field fieldB = new Field("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            ConstraintNode root = constraintNode()
                .where(fieldA).isContradictory()
                .withDecision(
                    constraintNode()
                        .where(fieldB).isNull(),
                    constraintNode()
                        .where(fieldB).isNull())
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields), "Description");

            //Act
            DecisionTree updatedTree = validator.reportThenCullContradictions(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(fullContradictionSubstring), any());
            assertNotEquals(tree, updatedTree);
        }

        @Test
        public void reportThenCullContradictions_forWhollyContradictoryProfileWithEveryNodeContradictory_reportsFullContradiction() {
            //Arrange
            Field fieldA = new Field("A");
            Field fieldB = new Field("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            ConstraintNode root = constraintNode()
                .where(fieldA).isContradictory()
                .withDecision(
                    constraintNode()
                        .where(fieldB).isContradictory(),
                    constraintNode()
                        .where(fieldB).isContradictory(),
                    constraintNode()
                        .where(fieldB).isContradictory())
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields), "Description");

            //Act
            DecisionTree updatedTree = validator.reportThenCullContradictions(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(fullContradictionSubstring), any());
            assertNotEquals(tree, updatedTree);
        }

        @Test
        public void reportThenCullContradictions_forWhollyContradictoryProfileWithContradictionDeepInBranch_reportsFullContradiction() {
            //Arrange
            Field fieldA = new Field("A");
            Field fieldB = new Field("B");
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

            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields), "Description");

            //Act
            DecisionTree updatedTree = validator.reportThenCullContradictions(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(fullContradictionSubstring), any());
            assertNotEquals(tree, updatedTree);
        }

        @Test
        public void reportThenCullContradictions_forWhollyContradictoryProfileWithAllContradictingNodes_reportsFullContradiction() {
            //Arrange
            Field fieldA = new Field("A");
            Field fieldB = new Field("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            ConstraintNode root = constraintNode()
                .where(fieldA).isContradictory()
                .withDecision(
                    constraintNode()
                        .where(fieldB).isContradictory(),
                    constraintNode()
                        .where(fieldB).isContradictory(),
                    constraintNode()
                        .where(fieldB).isContradictory())
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields), "Description");

            //Act
            DecisionTree updatedTree = validator.reportThenCullContradictions(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(fullContradictionSubstring), any());
            assertNotEquals(tree, updatedTree);
        }

        @Test
        public void reportThenCullContradictions_forWhollyContradictoryProfileWithNonContradictingRoot_reportsFullContradiction() {
            //Arrange
            Field fieldA = new Field("A");
            Field fieldB = new Field("B");
            List<Field> fields = new ArrayList<>();
            fields.add(fieldA);
            fields.add(fieldB);
            ConstraintNode root = constraintNode()
                .where(fieldA).isNull()
                .withDecision(
                    constraintNode()
                        .where(fieldB).isContradictory(),
                    constraintNode()
                        .where(fieldB).isContradictory(),
                    constraintNode()
                        .where(fieldB).isContradictory())
                .build();


            DecisionTree tree = new DecisionTree(root, new ProfileFields(fields), "Description");

            //Act
            DecisionTree updatedTree = validator.reportThenCullContradictions(tree, monitor);

            //Assert
            Mockito.verify(monitor, times(1))
                .addLineToPrintAtEndOfGeneration(contains(fullContradictionSubstring), any());
            assertNotEquals(tree, updatedTree);
        }
    }

    @Nested
    class unit_tests {
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
        public void reportContradictions_forNonContradictoryProfile_returnsNoContradiction() {
            //Arrange
            Mockito.when(checker.isContradictory(any(), any())).thenReturn(false);

            //Act
            ContradictionWrapper contradictionWrapper = validator.getContradictoryNodes(mockTree);

            //Assert
            assertTrue(contradictionWrapper.hasNoContradictions());
        }

        @Test
        public void reportContradictions_forWhollyContradictoryProfile_returnsWhollyContradictoryItem() {
            //Arrange
            Mockito.when(checker.isContradictory(any(), any())).thenReturn(false);
            Mockito.when(checker.isContradictory(rootNode, rootNode)).thenReturn(true);

            //Act
            ContradictionWrapper contradictionWrapper = validator.getContradictoryNodes(mockTree);

            //Assert
            assertTrue(contradictionWrapper.isWhollyContradictory(mockTree));
        }

        @Test
        public void reportContradictions_forPartiallyContradictoryProfileInFirstSubtree_returnsFirstSubtree() {
            //Arrange
            Mockito.when(checker.isContradictory(any(), any())).thenReturn(false);
            Mockito.when(checker.isContradictory(child0, child0)).thenReturn(true);


            //Act
            ContradictionWrapper contradictionWrapper = validator.getContradictoryNodes(mockTree);

            //Assert
            assertTrue(contradictionWrapper.isOnlyPartiallyContradictory(mockTree));
            assertEquals(1, contradictionWrapper.getContradictingNodes().size());
            assertEquals(child0, contradictionWrapper.getContradictingNodes().stream().findFirst().orElse(null));
        }


        @Test
        public void reportContradictions_forPartiallyContradictoryProfileInSecondSubtree_returnsSecondSubtree() {
            //Arrange
            Mockito.when(checker.isContradictory(any(), any())).thenReturn(false);
            Mockito.when(checker.isContradictory(child1, child1)).thenReturn(true);

            //Act
            ContradictionWrapper contradictionWrapper = validator.getContradictoryNodes(mockTree);

            //Assert
            assertTrue(contradictionWrapper.isOnlyPartiallyContradictory(mockTree));
            assertEquals(1, contradictionWrapper.getContradictingNodes().size());
            assertEquals(child1, contradictionWrapper.getContradictingNodes().stream().findFirst().orElse(null));
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
            ContradictionWrapper contradictionWrapper = validator.getContradictoryNodes(mockTree);

            //Assert
            assertTrue(contradictionWrapper.isOnlyPartiallyContradictory(mockTree));
            assertEquals(2, contradictionWrapper.getContradictingNodes().size());
            assertTrue(contradictionWrapper.getContradictingNodes().contains(child0));
            assertTrue(contradictionWrapper.getContradictingNodes().contains(child1));
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
        public void reportThenCullContradictions_withProfileWithNoContradictions_reportsNoContradictions() {
            //Arrange
            Mockito.when(checker.isContradictory(any(), any())).thenReturn(false);

            //Act
            DecisionTree checkedTree = validator.reportThenCullContradictions(mockTree, monitor);

            //Assert
            assertEquals(mockTree, checkedTree);
        }


        @Test
        public void reportThenCullContradictions_withPartiallyContradictingProfile_reportsPartialContradictions() {
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
}
