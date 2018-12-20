package com.scottlogic.deg.generator.generation.combination;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpec;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CombinationCreator {

    private final ConstraintReducer constraintReducer;
    private final GenerationConfig generationConfig;
    private final CombinationStrategy combinationStrategy;

    public CombinationCreator(ConstraintReducer constraintReducer, GenerationConfig generationConfig, CombinationStrategy combinationStrategy){
        this.constraintReducer = constraintReducer;
        this.generationConfig = generationConfig;
        this.combinationStrategy = combinationStrategy;
    }

    public List<Combination> makeCombinations(Collection<Field> fields, Collection<AtomicConstraint> constraints){
        Map<Field, FieldSpec> fieldSpecifications = getFieldSpecsForConstraints(fields, constraints);

        Map<Field, Stream<Object>> generatedData = fieldSpecifications.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry ->
                new FieldSpecValueGenerator(entry.getKey(), entry.getValue())
                    .generate(this.generationConfig)
                    .map(dataBag -> dataBag.getValue(entry.getKey()))
            ));

        return this.combinationStrategy.getCombinations(generatedData, fieldSpecifications);
    }

    private Map<Field, FieldSpec> getFieldSpecsForConstraints(Collection<Field> fields, Collection<AtomicConstraint> constraints){
        Map<Field, List<AtomicConstraint>> map = constraints.stream()
            .filter(c -> fields.contains(c.getField()))
            .collect(Collectors.groupingBy(AtomicConstraint::getField));

        return map.entrySet().stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> this.constraintReducer.reduceConstraintsToFieldSpec(entry.getValue()).orElse(FieldSpec.Empty)));
    }
}
