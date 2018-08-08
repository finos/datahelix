package com.scottlogic.deg.generator.reducer;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.restrictions.FieldSpec;
import com.scottlogic.deg.generator.restrictions.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.FieldSpecMerger;
import com.scottlogic.deg.generator.restrictions.RowSpec;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ConstraintReducer {
    private final ConstraintFieldSniffer constraintFieldSniffer = new ConstraintFieldSniffer();
    private final FieldSpecFactory fieldSpecFactory;
    private final FieldSpecMerger fieldSpecMerger;

    public ConstraintReducer(
            FieldSpecFactory fieldSpecFactory,
            FieldSpecMerger fieldSpecMerger
    ) {
        this.fieldSpecFactory = fieldSpecFactory;
        this.fieldSpecMerger = fieldSpecMerger;
    }

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

        final Map<Field, FieldSpec> fieldToFieldSpec = fields.stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                (field) ->  reduceConstraintsToFieldSpec(fieldToConstraints.get(field))
                        )
                );

        return new RowSpec(fields, fieldToFieldSpec);
    }

    private FieldSpec reduceConstraintsToFieldSpec(Iterable<IConstraint> constraints) {
        if (constraints == null) {
            return new FieldSpec();
        }
        return StreamSupport
            .stream(constraints.spliterator(), false)
            .map(fieldSpecFactory::construct)
            .reduce(new FieldSpec(), fieldSpecMerger::merge);
    }
}
