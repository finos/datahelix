package com.scottlogic.deg.generator.generation.combination;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpec;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DecisionTreeCombinationProducer implements CombinationProducer {

    private final DecisionTree tree;
    private final CombinationCreator combinationCreator;

    public DecisionTreeCombinationProducer(DecisionTree tree, CombinationCreator combinationCreator){
        this.tree = tree;
        this.combinationCreator = combinationCreator;
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
            return combinationCreator.makeCombinations(fields, currentAccumulation);
        }
        return node.getDecisions().stream()
            .flatMap(dNode -> this.getConstraintCombinations(dNode, currentAccumulation)
            .stream()).collect(Collectors.toList());
    }



}
