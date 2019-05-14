package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.constraint.atomic.AtomicConstraint;
import com.scottlogic.deg.common.constraint.atomic.AtomicConstraintsHelper;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.visualisation.BaseVisitor;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class FieldSpecExtractionVisitor extends BaseVisitor {

    public final HashSet<FieldSpec> fieldSpecs = new HashSet<>();
    private final Field field;
    private final ConstraintReducer constraintReducer;

    FieldSpecExtractionVisitor(Field field, ConstraintReducer constraintReducer) {
        this.field = field;
        this.constraintReducer = constraintReducer;
    }

    @Override
    public ConstraintNode visit(ConstraintNode constraintNode) {
        List<AtomicConstraint> atomicConstraintsForField =
            AtomicConstraintsHelper.getConstraintsForField(constraintNode.getAtomicConstraints(), field);

        Optional<FieldSpec> fieldSpec =
            atomicConstraintsForField.isEmpty()
                ? Optional.empty()
                : constraintReducer.reduceConstraintsToFieldSpec(atomicConstraintsForField);

        if (fieldSpec.isPresent()) {
            fieldSpecs.add(fieldSpec.get());
        }

        return constraintNode;
    }
}
