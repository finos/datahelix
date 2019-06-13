package com.scottlogic.deg.generator.validators;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContradictionChecker {
    private final ConstraintReducer constraintReducer;

    @Inject
    public ContradictionChecker(ConstraintReducer constraintReducer){
        this.constraintReducer = constraintReducer;
    }
    /**
     * Check for any contradictions
     * @param leftNode
     * @param rightNode
     * @return
     */
    public boolean checkContradictions(ConstraintNode leftNode, ConstraintNode rightNode){

        Collection<Field> leftFields = leftNode.getAtomicConstraints()
            .stream()
            .map(AtomicConstraint::getField)
            .collect(Collectors.toCollection(ArrayList::new));

        Collection<Field> rightFields = rightNode.getAtomicConstraints()
            .stream()
            .map(AtomicConstraint::getField)
            .collect(Collectors.toCollection(ArrayList::new));

        Collection<Field> combinedFields = Stream.concat(leftFields.stream(), rightFields.stream())
            .collect(Collectors.toCollection(ArrayList::new));

        Collection<AtomicConstraint> nodeToCheckConstraints = leftNode.getAtomicConstraints();
        Collection<AtomicConstraint> currentNodeConstraints = rightNode.getAtomicConstraints();

        Collection<AtomicConstraint> combinedConstraints = Stream.concat(
            nodeToCheckConstraints.stream(),
            currentNodeConstraints.stream()).collect(Collectors.toCollection(ArrayList::new));

        Map<Field, Collection<AtomicConstraint>> map = new HashMap<>();
        combinedConstraints.stream()
            .filter(constraint -> combinedFields.contains(constraint.getField()))
            .forEach(constraint -> addToConstraintsMap(map, constraint));

        for (Map.Entry<Field, Collection<AtomicConstraint>> entry : map.entrySet()) {
            Optional<FieldSpec> fieldSpec = constraintReducer.reduceConstraintsToFieldSpec(entry.getValue());

            if (!fieldSpec.isPresent()){
                return true;
            }
        }
        return false;
    }

    private void addToConstraintsMap(Map<Field, Collection<AtomicConstraint>> map, AtomicConstraint constraint) {
        if (!map.containsKey(constraint.getField())) {
            map.put(constraint.getField(), new ArrayList<>());
        }

        map.get(constraint.getField())
            .add(constraint);
    }
}
