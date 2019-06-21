package com.scottlogic.deg.generator.validators;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.*;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class ContradictionCheckerTests {
    private ContradictionChecker checker = new ContradictionChecker(new ConstraintReducer(new FieldSpecFactory(new StringRestrictionsFactory()), new FieldSpecMerger()));

    @Test
    public void checkContradictions_withContradictingNodes_returnContradiction() {
        //Arrange

        // shared
        Field fieldA = new Field("fieldA");
        AtomicConstraint isInteger = new IsOfTypeConstraint(fieldA, IsOfTypeConstraint.Types.NUMERIC, null);
        AtomicConstraint notNull = (new IsNullConstraint(fieldA, null)).negate();

        //left
        ConstraintNode leftNode = new TreeConstraintNode();
        Collection<AtomicConstraint> leftConstraints = new ArrayList<>();
        AtomicConstraint moreThan100 = new IsGreaterThanConstantConstraint(fieldA, 100, null);
        leftConstraints.add(moreThan100);
        leftConstraints.add(isInteger);
        leftConstraints.add(notNull);
        leftNode = leftNode.addAtomicConstraints(leftConstraints);

        //right
        ConstraintNode rightNode = new TreeConstraintNode();
        Collection<AtomicConstraint> rightConstraints = new ArrayList<>();
        AtomicConstraint lessThan50 = new IsLessThanConstantConstraint(fieldA, 50, null);
        rightConstraints.add(lessThan50);
        rightConstraints.add(isInteger);
        rightConstraints.add(notNull);
        rightNode = rightNode.addAtomicConstraints(rightConstraints);

        //Act
        boolean contradictionFound = checker.isContradictory(leftNode, rightNode);

        //Assert
        assertTrue(contradictionFound);
    }

    @Test
    public void checkContradictions_withNonContradictingNodes_returnNoContradiction() {
        //Arrange

        // shared
        Field fieldA = new Field("fieldA");
        AtomicConstraint isInteger = new IsOfTypeConstraint(fieldA, IsOfTypeConstraint.Types.NUMERIC, null);
        AtomicConstraint notNull = (new IsNullConstraint(fieldA, null)).negate();

        //left
        ConstraintNode leftNode = new TreeConstraintNode();
        Collection<AtomicConstraint> leftConstraints = new ArrayList<>();
        AtomicConstraint moreThan100 = new IsGreaterThanConstantConstraint(fieldA, 100, null);

        leftConstraints.add(moreThan100);
        leftConstraints.add(isInteger);
        leftConstraints.add(notNull);
        leftNode = leftNode.addAtomicConstraints(leftConstraints);

        //right
        ConstraintNode rightNode = new TreeConstraintNode();
        Collection<AtomicConstraint> rightConstraints = new ArrayList<>();
        AtomicConstraint moreThan1000 = new IsGreaterThanConstantConstraint(fieldA, 1000, null);
        rightConstraints.add(moreThan1000);
        rightConstraints.add(isInteger);
        rightConstraints.add(notNull);
        rightNode = rightNode.addAtomicConstraints(rightConstraints);

        //Act
        boolean contradictionFound = checker.isContradictory(leftNode, rightNode);

        //Assert
        assertFalse(contradictionFound);
    }
}
