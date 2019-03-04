package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.visualisation.BaseVisitor;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FieldSpecNodeVisitor extends BaseVisitor {

    public final HashSet<FieldSpec> fieldSpecs = new HashSet<>();
    private final Field field;
    private final ConstraintReducer constraintReducer;

    FieldSpecNodeVisitor(Field field, ConstraintReducer constraintReducer) {
        this.field = field;
        this.constraintReducer = constraintReducer;
    }

    @Override
    public ConstraintNode visit(ConstraintNode constraintNode) {
        List<AtomicConstraint> atomicConstraintsForField = constraintNode.getAtomicConstraints().stream()
            .filter(atomicConstraint -> atomicConstraint.getField().equals(field))
            .collect(Collectors.toList());

        Optional<FieldSpec> fieldSpec = constraintReducer.reduceConstraintsToFieldSpec(atomicConstraintsForField);

        if (fieldSpec.isPresent()) {
            fieldSpecs.add(fieldSpec.get());
        }

        return constraintNode;
    }
}
