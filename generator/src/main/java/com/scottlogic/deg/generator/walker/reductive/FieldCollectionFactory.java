package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecMerger;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.FixFieldStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldCollectionFactory {
    private final GenerationConfig config;
    private final ConstraintReducer constraintReducer;
    private final FieldSpecMerger fieldSpecMerger;
    private final FieldSpecFactory fieldSpecFactory;
    private final FixFieldStrategy fixFieldStrategy;
    private final ReductiveDataGeneratorMonitor monitor;

    public FieldCollectionFactory(
        GenerationConfig config,
        ConstraintReducer constraintReducer,
        FieldSpecMerger fieldSpecMerger,
        FieldSpecFactory fieldSpecFactory,
        FixFieldStrategy fixFieldStrategy,
        ReductiveDataGeneratorMonitor monitor) {
        this.config = config;
        this.constraintReducer = constraintReducer;
        this.fieldSpecMerger = fieldSpecMerger;
        this.fieldSpecFactory = fieldSpecFactory;
        this.fixFieldStrategy = fixFieldStrategy;
        this.monitor = monitor;
    }

    public FieldCollection create(DecisionTree tree){
        return new FieldCollection(
            tree.getFields(),
            this,
            this.config,
            this.constraintReducer,
            this.fieldSpecMerger,
            this.fieldSpecFactory,
            this.fixFieldStrategy,
            new HashMap<>(),
            null,
            this.monitor);
    }

    public FieldCollection create(FieldCollection fieldCollection, FixedField fixedField){
        Map<Field, FixedField> newFixedFieldsMap = fieldCollection.getLastFixedField() != null
            ? Stream.concat(
                fieldCollection.getFixedFields().entrySet().stream(),
                Stream.of(fieldCollection.getLastFixedField()))
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue))
            : fieldCollection.getFixedFields();

        return new FieldCollection(
            fieldCollection.getFields(),
            this,
            this.config,
            this.constraintReducer,
            this.fieldSpecMerger,
            this.fieldSpecFactory,
            this.fixFieldStrategy,
            newFixedFieldsMap,
            fixedField,
            this.monitor);
    }
}
