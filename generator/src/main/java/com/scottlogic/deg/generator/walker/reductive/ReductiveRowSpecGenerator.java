package com.scottlogic.deg.generator.walker.reductive;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecHelper;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy.FieldValue;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReductiveRowSpecGenerator {

    private final ConstraintReducer constraintReducer;
    private final FieldSpecMerger fieldSpecMerger;
    private final FieldSpecHelper fieldSpecHelper;
    private final ReductiveDataGeneratorMonitor monitor;

    @Inject
    public ReductiveRowSpecGenerator(
        ConstraintReducer constraintReducer,
        FieldSpecMerger fieldSpecMerger,
        FieldSpecHelper fieldSpecHelper,
        ReductiveDataGeneratorMonitor monitor) {
        this.fieldSpecMerger = fieldSpecMerger;
        this.constraintReducer = constraintReducer;
        this.fieldSpecHelper = fieldSpecHelper;
        this.monitor = monitor;
    }

    //produce a stream of RowSpecs for each value in the permitted set of values for the field fixed on the last iteration
    public Stream<RowSpec> createRowSpecsFromFixedValues(ReductiveState reductiveState) {
        Map<Field, FieldSpec> fieldSpecsPerField =
            reductiveState.getFieldValues().values().stream()
                .collect(Collectors.toMap(
                    FieldValue::getField,
                    fieldValue -> fieldSpecHelper.getFieldSpecForValue(fieldValue.getValue())));

        if (fieldSpecsPerField.values().stream().anyMatch(fieldSpec -> fieldSpec == FieldSpec.Empty)){
            this.monitor.unableToEmitRowAsSomeFieldSpecsAreEmpty(reductiveState, fieldSpecsPerField);
            return Stream.empty();
        }

        RowSpec rowSpec = new RowSpec(reductiveState.getFields(), fieldSpecsPerField);

        this.monitor.rowSpecEmitted(rowSpec);
        return Stream.of(rowSpec);
    }


}
