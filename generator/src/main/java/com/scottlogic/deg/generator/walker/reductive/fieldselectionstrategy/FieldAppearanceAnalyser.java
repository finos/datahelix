package com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.visualisation.BaseVisitor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FieldAppearanceAnalyser extends BaseVisitor {

    Map<Field, Integer> fieldAppearances = new HashMap<>();

    @Override
    public ConstraintNode visit(ConstraintNode constraintNode){
        constraintNode.getAtomicConstraints().stream()
            .map(AtomicConstraint::getField)
            .distinct()
            .forEach(this::countField);
        return constraintNode;
    }

    private void countField(Field field) {
        fieldAppearances.compute(field, (k, count) -> count == null ? 1 : count+1);
    }
}
