package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.reductive.ReductiveConstraintNode;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.generation.ReductiveDataGeneratorMonitor;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.*;
import com.scottlogic.deg.generator.walker.reductive.field_selection_strategy.FixFieldStrategy;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldCollectionHelper {

    private final GenerationConfig generationConfig;
    private final ConstraintReducer constraintReducer;
    private final FixFieldStrategy fixFieldStrategy;
    private final ReductiveDataGeneratorMonitor monitor;

    public FieldCollectionHelper(
        GenerationConfig config,
        ConstraintReducer constraintReducer,
        FixFieldStrategy fixFieldStrategy,
        ReductiveDataGeneratorMonitor monitor) {
        this.fixFieldStrategy = fixFieldStrategy;
        this.generationConfig = config;
        this.constraintReducer = constraintReducer;
        this.monitor = monitor;
    }

    //work out the next field to fix and return a new ReductiveState with this field fixed
    public FixedField findNextFixedField(ReductiveState reductiveState, ReductiveConstraintNode rootNode) {
        Field fieldToFix = fixFieldStrategy.getNextFieldToFix(reductiveState, rootNode);

        if (fieldToFix == null){
            throw new UnsupportedOperationException(
                String.format(
                    "Unable to find a field to fix, no finite constraints\nUnfixed fields: %s",
                    Objects.toString(reductiveState.getUnfixedFields())));
        }

        return createFixedFieldWithValues(fieldToFix, rootNode);
    }

    //for the given field get a stream of possible values
    private FixedField createFixedFieldWithValues(Field field, ConstraintNode rootNode) {
        //from the original tree, get all atomic constraints that match the given field
        Set<AtomicConstraint> constraintsForRootNode = rootNode.getAtomicConstraints()
            .stream()
            .filter(c -> c.getField().equals(field))
            .collect(Collectors.toSet());

        //produce a fieldspec for all the atomic constraints
        FieldSpec rootConstraintsFieldSpec = this.constraintReducer.reduceConstraintsToFieldSpec(constraintsForRootNode)
            .orElse(FieldSpec.Empty);

        //use the FieldSpecValueGenerator to emit all possible values given the generation mode, interesting or full-sequential
        Stream<Object> values = new FieldSpecValueGenerator(field, rootConstraintsFieldSpec)
            .generate(this.generationConfig)
            .map(dataBag -> dataBag.getValue(field));

        return new FixedField(field, values, rootConstraintsFieldSpec, this.monitor);
    }


}
