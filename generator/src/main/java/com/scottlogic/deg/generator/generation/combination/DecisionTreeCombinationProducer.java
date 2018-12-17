package com.scottlogic.deg.generator.generation.combination;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.rule_strategy.Combination;
import com.scottlogic.deg.generator.generation.FieldSpecFulfiller;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpec;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DecisionTreeCombinationProducer implements CombinationProducer {

    private final DecisionTree tree;
    private final ConstraintReducer reducer;
    private final GenerationConfig generationConfig;

    public DecisionTreeCombinationProducer(DecisionTree tree, ConstraintReducer reducer, GenerationConfig generationConfig){
        this.tree = tree;
        this.reducer = reducer;
        this.generationConfig = generationConfig;
    }

    @Override
    public Stream<Combination> getCombinations() {
        return this.getConstraintCombinations(tree.getRootNode()).stream();
    }

    private List<Combination> getConstraintCombinations(ConstraintNode root){
        return getConstraintCombinations(root, Collections.emptyList());
    }

    private List<Combination> getConstraintCombinations(DecisionNode node, Collection<AtomicConstraint> accumulatedConstraints){
        return node.getOptions().stream()
            .flatMap(option -> this.getConstraintCombinations(option, accumulatedConstraints).stream())
            .collect(Collectors.toList());
    }

    private List<Combination> getConstraintCombinations(ConstraintNode node, Collection<AtomicConstraint> accumulatedConstraints){
        Collection<AtomicConstraint> currentAccumulation = new HashSet<>(accumulatedConstraints);
        currentAccumulation.addAll(node.getAtomicConstraints());
        if (node.getDecisions().isEmpty()){
            Set<Field> fields = node.getAtomicConstraints().stream().map(AtomicConstraint::getField).collect(Collectors.toSet());
            return this.makeCombinations(fields, currentAccumulation);
        }
        return node.getDecisions().stream()
            .flatMap(dNode -> this.getConstraintCombinations(dNode, currentAccumulation)
            .stream()).collect(Collectors.toList());
    }

    private List<Combination> makeCombinations(Collection<Field> fields, Collection<AtomicConstraint> constraints){
        Map<Field, FieldSpec> fieldSpecifications = getFieldSpecsForConstraints(fields, constraints);

        Map<Field, Stream<Object>> generatedData = fieldSpecifications.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry ->
                new FieldSpecFulfiller(entry.getKey(), entry.getValue())
                    .generate(this.generationConfig)
                    .map(dataBag -> dataBag.getValue(entry.getKey()))
            ));

        return getCombinations(generatedData);
    }

    private Map<Field, FieldSpec> getFieldSpecsForConstraints(Collection<Field> fields, Collection<AtomicConstraint> constraints){
        return constraints
            .stream()
            .filter(c -> fields.contains(c.getField()))
            .collect(Collectors.groupingBy(AtomicConstraint::getField))
            .entrySet()
            .stream()
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> this.reducer.reduceConstraintsToFieldSpec(entry.getValue()).orElse(FieldSpec.Empty)));
    }

    private List<Combination> getCombinations(Map<Field, Stream<Object>> generatedData) {
        Combination combo = new Combination();
        generatedData.forEach((k, v) -> {
            combo.add(k, v.findFirst().orElse(null));
        });
        return Collections.singletonList(combo);
    }

}
