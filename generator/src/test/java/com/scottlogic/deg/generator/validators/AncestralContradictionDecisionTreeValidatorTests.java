package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.TreeConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.inputs.validation.ValidationAlert;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class AncestralContradictionDecisionTreeValidatorTests {

    private AncestralContradictionDecisionTreeValidator validator;
    @BeforeEach
    void setup() {
        validator =
            new AncestralContradictionDecisionTreeValidator(
                new ConstraintReducer(
                    new FieldSpecFactory(
                        new FieldSpecMerger(),
                        new StringRestrictionsFactory()),
                    new FieldSpecMerger()));
    }

//    @Test
//    public void check_decisiontreeWithNoContradictions_returnsNoContradictions() {
//        //Arrange
//        DecisionTree dt = new DecisionTree();
//        //Act
//        //Collection<ValidationAlert> actualResult = validator.preProfileChecks(config, mockConfigSource);
//
//        //Assert
//        //Assert.assertThat(actualResult, empty());
//    }

    @Test
    public void check_contradictingNodes_returnContradiction(){
        //Arrange

        // shared
        Field fieldA = new Field("fieldA");
        AtomicConstraint isInteger = new IsOfTypeConstraint(fieldA, IsOfTypeConstraint.Types.NUMERIC, null);
        AtomicConstraint notNull = new NotConstraint(new IsNullConstraint(fieldA, null));

        // left
        ConstraintNode leftNode = new TreeConstraintNode();
        Collection<AtomicConstraint> leftConstraints = new ArrayList<>();
        AtomicConstraint moreThan100 = new IsGreaterThanConstantConstraint(fieldA,100, null);
        leftConstraints.add(moreThan100);
        leftConstraints.add(isInteger);
        leftConstraints.add(notNull);
        leftNode = leftNode.addAtomicConstraints(leftConstraints);

        // right
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

        // left
        ConstraintNode leftNode = new TreeConstraintNode();
        Collection<AtomicConstraint> leftConstraints = new ArrayList<>();
        AtomicConstraint moreThan100 = new IsGreaterThanConstantConstraint(fieldA,100, null);

        leftConstraints.add(moreThan100);
        leftConstraints.add(isInteger);
        leftConstraints.add(notNull);
        leftNode = leftNode.addAtomicConstraints(leftConstraints);

        // right
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