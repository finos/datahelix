package com.scottlogic.deg.generator.generation.combination;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.generator.constraints.atomic.ViolatedAtomicConstraint;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.generator.decisiontree.visualisation.BaseVisitor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ViolationCombinationProducer implements CombinationProducer {

    private final DecisionTree decisionTree;
    private final CombinationCreator combinationCreator;

    public ViolationCombinationProducer(DecisionTree decisionTree, CombinationCreator combinationCreator){
        this.decisionTree = decisionTree;
        this.combinationCreator = combinationCreator;
    }

    @Override
    public Stream<Combination> getCombinations() {

        Collection<AtomicConstraint> violatedRootConstraints = decisionTree.getRootNode().getAtomicConstraints()
            .stream().filter(x->x instanceof ViolatedAtomicConstraint)
            .collect(Collectors.toSet());

        if (!violatedRootConstraints.isEmpty()){
            return combinationCreator.makeCombinations(getFields(violatedRootConstraints), violatedRootConstraints).stream();
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

        return combinationCreator.makeCombinations(getFields(constraintNode.getAtomicConstraints()), constraints);
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
