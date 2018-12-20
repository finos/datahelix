package com.scottlogic.deg.generator.generation.combination;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.atomic.ViolatedAtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.decisiontree.visualisation.BaseVisitor;
import com.scottlogic.deg.generator.generation.FieldSpecValueGenerator;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.reducer.ConstraintReducer;
import com.scottlogic.deg.generator.restrictions.FieldSpec;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ViolationCombinationProducer implements CombinationProducer {

    private final DecisionTree decisionTree;
    private final ConstraintReducer constraintReducer;
    private final GenerationConfig generationConfig;
    private final CombinationStrategy combinationStrategy;

    public ViolationCombinationProducer(DecisionTree decisionTree, ConstraintReducer constraintReducer, GenerationConfig generationConfig, CombinationStrategy combinationStrategy){
        this.decisionTree = decisionTree;
        this.constraintReducer = constraintReducer;
        this.generationConfig = generationConfig;
        this.combinationStrategy = combinationStrategy;
    }

    @Override
    public Stream<Combination> getCombinations() {

        Collection<AtomicConstraint> violatedRootConstraints = decisionTree.getRootNode().getAtomicConstraints()
            .stream().filter(x->x instanceof ViolatedAtomicConstraint)
            .collect(Collectors.toSet());

        if (!violatedRootConstraints.isEmpty()){
            return makeCombinations(getFields(violatedRootConstraints), violatedRootConstraints).stream();
        }

        DecisionNode violated = getViolatedDecisionNode(decisionTree.getRootNode());
        if (violated == null){
            // no decision has been violated
            return Stream.empty();
        }

        List<Combination> combinations = new ArrayList<>();
        for (ConstraintNode option : violated.getOptions()) {
            combinations.addAll(combinationsForOption(option));
        }

        return combinations.stream();
    }

    private Set<Field> getFields(Collection<AtomicConstraint> violatedRootConstraints) {
        return violatedRootConstraints.stream()
                    .map(AtomicConstraint::getField)
                    .collect(Collectors.toSet());
    }


    private List<Combination> combinationsForOption(ConstraintNode constraintNode){
        Set<AtomicConstraint> constraints = Stream.concat(
            decisionTree.rootNode.getAtomicConstraints().stream(),
            constraintNode.getAtomicConstraints().stream())
            .collect(Collectors.toSet());

        return makeCombinations(getFields(constraintNode.getAtomicConstraints()), constraints);
    }

    private List<Combination> makeCombinations(Collection<Field> fields, Collection<AtomicConstraint> constraints){
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


    private DecisionNode getViolatedDecisionNode(ConstraintNode constraintNode) {
        for (DecisionNode decision : constraintNode.getDecisions()) {
            ViolationFinder violationFinder = new ViolationFinder();
            decision.accept(violationFinder);
            if (violationFinder.foundViolation){
                return decision;
            }
        }

        return null;
    }


    private class ViolationFinder extends BaseVisitor{
        boolean foundViolation;

        @Override
        public AtomicConstraint visit(AtomicConstraint atomicConstraint) {
            if (atomicConstraint instanceof ViolatedAtomicConstraint){
                foundViolation = true;
            }
            return atomicConstraint;
        }
    }
}
