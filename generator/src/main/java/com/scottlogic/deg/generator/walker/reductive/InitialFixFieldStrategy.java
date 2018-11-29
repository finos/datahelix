package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.reducer.ConstraintFieldSniffer;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InitialFixFieldStrategy implements FixFieldStrategy {

    private final ConstraintFieldSniffer fieldSniffer;

    public InitialFixFieldStrategy(ConstraintFieldSniffer fieldSniffer) {
        this.fieldSniffer = fieldSniffer;
    }

    @Override
    public FieldAndConstraintMapping getFieldAndConstraintMapToFixNext(ReductiveConstraintNode rootNode) {
        Map<Field, List<IConstraint>> constraints = rootNode.getAllIncludedConstraints()
            .stream()
            .collect(Collectors.groupingBy(this.fieldSniffer::detectField));

        return constraints.entrySet()
            .stream()
            .map(entry -> new FieldAndConstraintMapping(entry.getKey(), entry.getValue()))
            .max(Comparator.comparing(FieldAndConstraintMapping::getPriority))
            .filter(c -> !c.getConstraints().isEmpty())
            .orElse(null);
    }
}
