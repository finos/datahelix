package com.scottlogic.deg.generator.reducer;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.restrictions.FieldSpec;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ConstraintReducer {
    private final ConstraintFieldSniffer constraintFieldSniffer = new ConstraintFieldSniffer();
    private final FieldSpecFactory fieldSpecFactory = new FieldSpecFactory();
    private final FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();

    public RowSpec reduceConstraintsToRowSpec(ProfileFields fields, Iterable<IConstraint> constraints) {
        final Map<Field, List<IConstraint>> fieldToConstraints = StreamSupport
            .stream(constraints.spliterator(), false)
            .map(constraintFieldSniffer::generateTuple)
            .collect(
                Collectors.groupingBy(
                    ConstraintAndFieldTuple::getField, // map from field...
                    Collectors.mapping( // ...to a list of constraints
                        ConstraintAndFieldTuple::getConstraint,
                        Collectors.toList())));

        Map<Field, FieldSpec> fieldToFieldSpec = new HashMap<>();
        for (Field field : fields) {
            fieldToFieldSpec.put(
                field,
                this.reduceConstraintsToFieldSpec(
                    fieldToConstraints.get(field)));
        }

        return new RowSpec(fields, fieldToFieldSpec);
    }

    private FieldSpec reduceConstraintsToFieldSpec(Iterable<IConstraint> constraints) {
        return StreamSupport
            .stream(constraints.spliterator(), false)
            .map(fieldSpecFactory::construct)
            .reduce(fieldSpecMerger::merge)
            .orElseThrow(() -> new IllegalStateException(""));
    }
}
