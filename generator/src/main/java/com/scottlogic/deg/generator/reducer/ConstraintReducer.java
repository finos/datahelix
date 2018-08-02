package com.scottlogic.deg.generator.reducer;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.restrictions.FieldSpec;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpec;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ConstraintReducer {
    private final ConstraintFieldSniffer constraintFieldSniffer = new ConstraintFieldSniffer();
    private final FieldSpecFactory fieldSpecFactory = new FieldSpecFactory();
    private final FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();

    public RowSpec getReducedConstraints(Iterable<IConstraint> constraints) {
        final Map<Field, List<ConstraintAndFieldTuple>> fieldConstraints = StreamSupport
                .stream(constraints.spliterator(), false)
                .map(constraintFieldSniffer::generateTuple)
                .collect(Collectors.groupingBy(ConstraintAndFieldTuple::getField));
        final List<FieldSpec> fieldSpecs = fieldConstraints.entrySet().stream()
                .map(
                        x -> getReducedConstraints(
                                x.getKey(),
                                x.getValue()
                                        .stream()
                                        .map(ConstraintAndFieldTuple::getConstraint)
                                        .collect(Collectors.toList())
                        )
                )
                .collect(Collectors.toList());
        return new RowSpec(fieldSpecs);
    }

    private FieldSpec getReducedConstraints(Field field, Iterable<IConstraint> constraints) {
        return StreamSupport
                .stream(constraints.spliterator(), false)
                .map(constraint -> fieldSpecFactory.construct(field.name, constraint))
                .reduce(fieldSpecMerger::merge)
        .orElseThrow(() -> new IllegalStateException(""));
    }

}

