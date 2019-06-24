package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraintsHelper;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.utils.SetUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContradictionChecker {
    private final ConstraintReducer constraintReducer;

    @Inject
    public ContradictionChecker(ConstraintReducer constraintReducer){
        this.constraintReducer = constraintReducer;
    }

    public boolean isContradictory(ConstraintNode leftNode, ConstraintNode rightNode){

        Set<Field> fields = SetUtils.union(getFields(leftNode), getFields(rightNode));

        Collection<AtomicConstraint> constraints = Stream.concat(
            leftNode.getAtomicConstraints().stream(),
            rightNode.getAtomicConstraints().stream()).collect(Collectors.toSet());

        return fields.stream()
            .map(field -> AtomicConstraintsHelper.getConstraintsForField(constraints, field))
            .map(constraintReducer::reduceConstraintsToFieldSpec)
            .noneMatch(Optional::isPresent);

    }

    private Set<Field> getFields(ConstraintNode leftNode) {
        return leftNode.getAtomicConstraints()
            .stream()
            .map(AtomicConstraint::getField)
            .collect(Collectors.toSet());
    }
}
