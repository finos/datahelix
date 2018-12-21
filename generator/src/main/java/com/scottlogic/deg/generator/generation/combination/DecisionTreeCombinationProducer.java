package com.scottlogic.deg.generator.generation.combination;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Produces combinations of values based on a decision tree.
 *
 * Will produce combinations for each field in the root node and combinations for each leaf node.
 */
public final class DecisionTreeCombinationProducer implements CombinationProducer {

    private final DecisionTree tree;
    private final CombinationCreator combinationCreator;

    public DecisionTreeCombinationProducer(DecisionTree tree, CombinationCreator combinationCreator){
        this.tree = tree;
        this.combinationCreator = combinationCreator;
    }

    @Override
    public Stream<Combination> getCombinations() {
        return this.getConstraintCombinations(tree.getRootNode());
    }

    private Stream<Combination> getConstraintCombinations(ConstraintNode root){
        List<Combination> rootCombinations = getConstrainedFields(root).stream()
            .flatMap(field -> combinationCreator.makeCombinations(Collections.singletonList(field), root.getAtomicConstraints()).stream())
            .collect(Collectors.toList());

        return Stream.concat(rootCombinations.stream(), getConstraintCombinationsFromLeaves(root, Collections.emptyList()).stream());
    }

    private List<Combination> getConstraintCombinationsFromLeaves(DecisionNode node, Collection<AtomicConstraint> accumulatedConstraints){
        return node.getOptions().stream()
            .flatMap(option -> this.getConstraintCombinationsFromLeaves(option, accumulatedConstraints).stream())
            .collect(Collectors.toList());
    }

    private List<Combination> getConstraintCombinationsFromLeaves(ConstraintNode node, Collection<AtomicConstraint> accumulatedConstraints){
        Collection<AtomicConstraint> currentAccumulation = new HashSet<>(accumulatedConstraints);
        currentAccumulation.addAll(node.getAtomicConstraints());
        if (node.getDecisions().isEmpty()) {
            Collection<Field> fields = getConstrainedFields(node);
            return combinationCreator.makeCombinations(fields, currentAccumulation);
        }
        return node.getDecisions().stream()
            .flatMap(dNode -> this.getConstraintCombinationsFromLeaves(dNode, currentAccumulation)
            .stream()).collect(Collectors.toList());
    }

    public static Collection<Field> getConstrainedFields(ConstraintNode node) {
        return node.getAtomicConstraints().stream().map(AtomicConstraint::getField).collect(Collectors.toSet());
    }

}
