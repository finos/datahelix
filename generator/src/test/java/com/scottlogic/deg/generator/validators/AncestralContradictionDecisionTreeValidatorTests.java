package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.*;

public class AncestralContradictionDecisionTreeValidatorTests {

    private AncestralContradictionDecisionTreeValidator validator;
    private Field fieldA;
    private Field fieldB;
    private Field fieldC;
    private ContradictionValidationMonitor outputter;

    @BeforeEach
    void setup() {
        fieldA = new Field("fieldA");
        fieldB = new Field("fieldB");
        fieldC = new Field("fieldC");

        outputter = mock(ContradictionValidationMonitor.class);

        validator =
            new AncestralContradictionDecisionTreeValidator(
                new ConstraintReducer(
                    new FieldSpecFactory(
                        new FieldSpecMerger(),
                        new StringRestrictionsFactory()),
                    new FieldSpecMerger()),
                outputter);
    }

    @Test
    public void check_decisionTreeWithNoContradictions_doesNotOutputContradictionToConsole() {
        //Arrange
        ArrayList<Field> fieldList = new ArrayList<>();
        fieldList.add(fieldA);

        ConstraintNode rootNode = new TreeConstraintNode();
        DecisionNode levelOneDecision = new TreeDecisionNode();
        ConstraintNode leafNodeLeft  = new TreeConstraintNode();
        ConstraintNode leafNodeRight  = new TreeConstraintNode();

        AtomicConstraint isInteger = new IsOfTypeConstraint(fieldA, IsOfTypeConstraint.Types.NUMERIC, null);
        AtomicConstraint notNull = new NotConstraint(new IsNullConstraint(fieldA, null));

        //leafNodeLeft
        Collection<AtomicConstraint> leftConstraints = new ArrayList<>();
        AtomicConstraint moreThan100 = new IsGreaterThanConstantConstraint(fieldA,100, null);
        leftConstraints.add(moreThan100);
        leftConstraints.add(isInteger);
        leftConstraints.add(notNull);
        leafNodeLeft = leafNodeLeft.addAtomicConstraints(leftConstraints);

        // leafNodeRight
        Collection<AtomicConstraint> rightConstraints = new ArrayList<>();
        AtomicConstraint moreThan50 = new IsGreaterThanConstantConstraint(fieldA,50, null);
        rightConstraints.add(moreThan50);
        rightConstraints.add(isInteger);
        rightConstraints.add(notNull);
        leafNodeRight = leafNodeRight.addAtomicConstraints(rightConstraints);

        // compose nodes
        Collection<ConstraintNode> constraintNodes = new ArrayList<>();
        constraintNodes.add(leafNodeLeft);
        constraintNodes.add(leafNodeRight);

        levelOneDecision = levelOneDecision.setOptions(constraintNodes);
        rootNode = rootNode.addDecisions(new ArrayList<>(Arrays.asList(levelOneDecision)));

        ProfileFields profileFields = new ProfileFields(fieldList);

        DecisionTree dt = new DecisionTree(rootNode, profileFields,"A Test Profile");

        //Act
        validator.reportContradictions(dt);

        //Assert
        //verify contradictionInTree outputter method is never called.
        verify(outputter, never()).contradictionInTree(anyObject());
    }

    @Test
    public void check_decisionTreeWithContradictions_whichAreNotOfADescendant_doesNotOutputContradictionToConsole() {
        //Arrange
        ArrayList<Field> fieldList = new ArrayList<>();
        fieldList.add(fieldA);

        ConstraintNode rootNode = new TreeConstraintNode();
        DecisionNode levelOneDecision = new TreeDecisionNode();
        ConstraintNode leafNodeLeft  = new TreeConstraintNode();
        ConstraintNode leafNodeRight  = new TreeConstraintNode();

        AtomicConstraint isInteger = new IsOfTypeConstraint(fieldA, IsOfTypeConstraint.Types.NUMERIC, null);
        AtomicConstraint notNull = new NotConstraint(new IsNullConstraint(fieldA, null));

        //leafNodeLeft
        Collection<AtomicConstraint> leftConstraints = new ArrayList<>();
        AtomicConstraint moreThan100 = new IsGreaterThanConstantConstraint(fieldA,100, null);
        leftConstraints.add(moreThan100);
        leftConstraints.add(isInteger);
        leftConstraints.add(notNull);
        leafNodeLeft = leafNodeLeft.addAtomicConstraints(leftConstraints);

        //leafNodeRight
        Collection<AtomicConstraint> rightConstraints = new ArrayList<>();
        AtomicConstraint lessThan50 = new IsLessThanConstantConstraint(fieldA,50, null);
        rightConstraints.add(lessThan50);
        rightConstraints.add(isInteger);
        rightConstraints.add(notNull);
        leafNodeRight = leafNodeRight.addAtomicConstraints(rightConstraints);

        //compose nodes
        Collection<ConstraintNode> constraintNodes = new ArrayList<>();
        constraintNodes.add(leafNodeLeft);
        constraintNodes.add(leafNodeRight);

        levelOneDecision = levelOneDecision.setOptions(constraintNodes);
        rootNode = rootNode.addDecisions(new ArrayList<>(Arrays.asList(levelOneDecision)));

        ProfileFields profileFields = new ProfileFields(fieldList);

        DecisionTree dt = new DecisionTree(rootNode, profileFields,"A Test Profile");

        //Act
        validator.reportContradictions(dt);

        //verify contradictionInTree outputter method is never called.
        verify(outputter, never()).contradictionInTree(anyObject());
    }

    @Test
    public void check_decisionTreeWithContradictions_whichAreOfADescendant_OutputsContradictionToConsole() {
        //Arrange
        ArrayList<Field> fieldList = new ArrayList<>();
        fieldList.add(fieldA);

        ConstraintNode rootNode = new TreeConstraintNode();
        DecisionNode decisionL1 = new TreeDecisionNode();
        DecisionNode decisionL2 = new TreeDecisionNode();
        ConstraintNode l1ConstraintNodeLeft  = new TreeConstraintNode();
        ConstraintNode l1ConstraintNodeRight  = new TreeConstraintNode();
        ConstraintNode l2ConstraintNodeLeft  = new TreeConstraintNode();
        ConstraintNode l2ConstraintNodeRight  = new TreeConstraintNode();

        AtomicConstraint fieldAIsInteger = new IsOfTypeConstraint(fieldA, IsOfTypeConstraint.Types.NUMERIC, null);
        AtomicConstraint fieldANotNull = new NotConstraint(new IsNullConstraint(fieldA, null));

        //L1ConstraintNodeLeft
        Collection<AtomicConstraint> l1leftConstraints = new ArrayList<>();
        AtomicConstraint fieldALessThan50 = new IsLessThanConstantConstraint(fieldA,50, null);
        l1leftConstraints.add(fieldALessThan50);
        l1leftConstraints.add(fieldAIsInteger);
        l1leftConstraints.add(fieldANotNull);
        l1ConstraintNodeLeft = l1ConstraintNodeLeft.addAtomicConstraints(l1leftConstraints);

        //L1ConstraintNodeRight
        Collection<AtomicConstraint> l1rightConstraints = new ArrayList<>();
        AtomicConstraint lessThan50 = new IsLessThanConstantConstraint(fieldB,50, null);
        l1rightConstraints.add(lessThan50);
        l1ConstraintNodeRight = l1ConstraintNodeRight.addAtomicConstraints(l1rightConstraints);

        //L2ConstraintNodeLeft
        Collection<AtomicConstraint> l2LeftConstraints = new ArrayList<>();
        AtomicConstraint moreThan1000 = new IsGreaterThanConstantConstraint(fieldA,1000, null);
        l2LeftConstraints.add(moreThan1000);
        l2LeftConstraints.add(fieldAIsInteger);
        l2LeftConstraints.add(fieldANotNull);
        l2ConstraintNodeLeft = l2ConstraintNodeLeft.addAtomicConstraints(l2LeftConstraints);

        //L2ConstraintNodeRight
        Collection<AtomicConstraint> l2RightConstraints = new ArrayList<>();
        AtomicConstraint fieldCLessThan5 = new IsLessThanConstantConstraint(fieldC,5, null);
        l2RightConstraints.add(fieldCLessThan5);
        l2ConstraintNodeRight = l2ConstraintNodeRight.addAtomicConstraints(l2RightConstraints);

        //compose nodes level 2
        Collection<ConstraintNode> constraintNodesL2 = new ArrayList<>();
        constraintNodesL2.add(l2ConstraintNodeLeft);
        constraintNodesL2.add(l2ConstraintNodeRight);
        decisionL2 = decisionL2.setOptions(constraintNodesL2);
        l1ConstraintNodeLeft = l1ConstraintNodeLeft.addDecisions(new ArrayList<>(Arrays.asList(decisionL2)));

        //compose nodes level 1
        Collection<ConstraintNode> constraintNodesL1 = new ArrayList<>();
        constraintNodesL1.add(l1ConstraintNodeLeft);
        constraintNodesL1.add(l1ConstraintNodeRight);
        decisionL1 = decisionL1.setOptions(constraintNodesL1);

        //compose nodes level 2
        rootNode = rootNode.addDecisions(new ArrayList<>(Arrays.asList(decisionL1)));
        ProfileFields profileFields = new ProfileFields(fieldList);
        DecisionTree dt = new DecisionTree(rootNode, profileFields,"A Test Profile");

        //Act
        validator.reportContradictions(dt);

        //Assert
        //verify contradictionInTree outputter method is never called.
        verify(outputter).contradictionInTree(anyObject());

    }

    @Test
    public void check_contradictingNodes_returnContradiction(){
        //Arrange

        // shared
        Field fieldA = new Field("fieldA");
        AtomicConstraint isInteger = new IsOfTypeConstraint(fieldA, IsOfTypeConstraint.Types.NUMERIC, null);
        AtomicConstraint notNull = new NotConstraint(new IsNullConstraint(fieldA, null));

        //left
        ConstraintNode leftNode = new TreeConstraintNode();
        Collection<AtomicConstraint> leftConstraints = new ArrayList<>();
        AtomicConstraint moreThan100 = new IsGreaterThanConstantConstraint(fieldA,100, null);
        leftConstraints.add(moreThan100);
        leftConstraints.add(isInteger);
        leftConstraints.add(notNull);
        leftNode = leftNode.addAtomicConstraints(leftConstraints);

        //right
        ConstraintNode rightNode = new TreeConstraintNode();
        Collection<AtomicConstraint> rightConstraints = new ArrayList<>();
        AtomicConstraint lessThan50 = new IsLessThanConstantConstraint(fieldA,50, null);
        rightConstraints.add(lessThan50);
        rightConstraints.add(isInteger);
        rightConstraints.add(notNull);
        rightNode = rightNode.addAtomicConstraints(rightConstraints);

        //Act
        boolean contradictionFound = validator.checkContradictions(leftNode, rightNode);

        //Assert
        Assert.assertTrue(contradictionFound);
    }

    @Test
    public void check_nonContradictingNodes_returnNoContradiction(){
        //Arrange

        // shared
        Field fieldA = new Field("fieldA");
        AtomicConstraint isInteger = new IsOfTypeConstraint(fieldA, IsOfTypeConstraint.Types.NUMERIC, null);
        AtomicConstraint notNull = new NotConstraint(new IsNullConstraint(fieldA, null));

        //left
        ConstraintNode leftNode = new TreeConstraintNode();
        Collection<AtomicConstraint> leftConstraints = new ArrayList<>();
        AtomicConstraint moreThan100 = new IsGreaterThanConstantConstraint(fieldA,100, null);

        leftConstraints.add(moreThan100);
        leftConstraints.add(isInteger);
        leftConstraints.add(notNull);
        leftNode = leftNode.addAtomicConstraints(leftConstraints);

        //right
        ConstraintNode rightNode = new TreeConstraintNode();
        Collection<AtomicConstraint> rightConstraints = new ArrayList<>();
        AtomicConstraint moreThan1000 = new IsGreaterThanConstantConstraint(fieldA,1000, null);
        rightConstraints.add(moreThan1000);
        rightConstraints.add(isInteger);
        rightConstraints.add(notNull);
        rightNode = rightNode.addAtomicConstraints(rightConstraints);

        //Act
        boolean contradictionFound = validator.checkContradictions(leftNode, rightNode);

        //Assert
        Assert.assertFalse(contradictionFound);
    }
}