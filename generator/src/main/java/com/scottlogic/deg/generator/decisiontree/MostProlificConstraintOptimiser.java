/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.generator.decisiontree;

import com.scottlogic.deg.common.profile.constraints.atomic.AtomicConstraint;
import com.scottlogic.deg.common.profile.constraints.atomic.NotConstraint;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MostProlificConstraintOptimiser implements DecisionTreeOptimiser {
    private final int maxIterations = 50;

    @Override
    public DecisionTree optimiseTree(DecisionTree tree){
        ConstraintNode newRootNode = optimiseLevelOfTree(tree.getRootNode());
        return new DecisionTree(newRootNode, tree.getFields());
    }

    private ConstraintNode optimiseLevelOfTree(ConstraintNode rootNode){
        for (int iteration = 0; iteration < maxIterations; iteration++) {
            ConstraintNode newRootNode = optimiseDecisions(rootNode);

            if (noChangeInDecisionCount(rootNode, newRootNode)) {
                return newRootNode;
            }

            rootNode = newRootNode;
        }

        return rootNode;
    }

    private boolean noChangeInDecisionCount(ConstraintNode rootNode, ConstraintNode newRootNode) {
        return newRootNode.getDecisions().size() == rootNode.getDecisions().size();
    }

    private ConstraintNode optimiseDecisions(ConstraintNode rootNode){
        AtomicConstraint mostProlificAtomicConstraint = getMostProlificAtomicConstraint(rootNode.getDecisions());
        if (mostProlificAtomicConstraint == null){
            return rootNode;
        }
        // Add negation of most prolific constraint to new decision node
        AtomicConstraint negatedMostProlificConstraint = mostProlificAtomicConstraint.negate();

        List<DecisionNode> factorisableDecisionNodes = rootNode.getDecisions().stream()
            .filter(node -> this.decisionIsFactorisable(node, mostProlificAtomicConstraint, negatedMostProlificConstraint))
            .collect(Collectors.toList());
        if (factorisableDecisionNodes.size() < 2){
            return rootNode;
        }

        // Add most prolific constraint to new decision node
        ConstraintNode factorisingConstraintNode = new ConstraintNode(mostProlificAtomicConstraint);
        ConstraintNode negatedFactorisingConstraintNode = new ConstraintNode(negatedMostProlificConstraint);

        Set<ConstraintNode> otherOptions = new HashSet<>();
        Set<DecisionNode> decisionsToRemove = new HashSet<>();

        for (DecisionNode decision : factorisableDecisionNodes) {
            DecisionAnalyser analyser = new DecisionAnalyser(decision, mostProlificAtomicConstraint);
            DecisionAnalysisResult result = analyser.performAnalysis();

            // Perform movement of options
            factorisingConstraintNode = addOptionsAsDecisionUnderConstraintNode(factorisingConstraintNode, result.optionsToFactorise);
            negatedFactorisingConstraintNode = addOptionsAsDecisionUnderConstraintNode(negatedFactorisingConstraintNode, result.negatedOptionsToFactorise);
            otherOptions.addAll(result.adjacentOptions);
            decisionsToRemove.add(decision);
        }

        // Add new decision node
        DecisionNode factorisedDecisionNode = new DecisionNode(
            Stream.concat(
                Stream.of(
                    optimiseLevelOfTree(factorisingConstraintNode),
                    optimiseLevelOfTree(negatedFactorisingConstraintNode)),
                otherOptions.stream())
            .collect(Collectors.toList()));

        return rootNode
            .removeDecisions(decisionsToRemove)
            .addDecisions(Collections.singletonList(factorisedDecisionNode));
    }

    private boolean constraintNodeContainsNegatedConstraints(ConstraintNode node, Set<AtomicConstraint> constraints){
        return node.getAtomicConstraints().stream()
            .map(AtomicConstraint::negate)
            .allMatch(constraints::contains);
    }

    private ConstraintNode addOptionsAsDecisionUnderConstraintNode(
        ConstraintNode newNode,
        Collection<ConstraintNode> optionsToAdd) {
        if (optionsToAdd.isEmpty()) {
            return newNode;
        }

        DecisionNode decisionUnderFactorisedNode = new DecisionNode(optionsToAdd);
        return newNode.addDecisions(Collections.singletonList(decisionUnderFactorisedNode));
    }

    private int disfavourNotConstraints(Map.Entry<AtomicConstraint, List<AtomicConstraint>> entry){
        return entry.getKey() instanceof NotConstraint ? 1 : 0;
    }

    private AtomicConstraint getMostProlificAtomicConstraint(Collection<DecisionNode> decisions) {
        Map<AtomicConstraint, List<AtomicConstraint>> decisionConstraints =
                decisions.stream()
                    .flatMap(dn -> dn.getOptions().stream())
                    .flatMap(option -> option.getAtomicConstraints().stream())
                    .collect(Collectors.groupingBy(Function.identity()));

        Comparator<Map.Entry<AtomicConstraint, List<AtomicConstraint>>> comparator = Comparator
            .comparing(entry -> entry.getValue().size());
        comparator = comparator.reversed()
            .thenComparing(this::disfavourNotConstraints)
            .thenComparing(entry -> entry.getKey().toString());

        return decisionConstraints.entrySet()
            .stream()
            .filter(constraint -> constraint.getValue().size() > 1) // where the number of occurrences > 1
            .sorted(comparator)
            .map(entry -> getConstraint(entry.getValue()))
            .findFirst()
            .orElse(null); //otherwise return null
    }

    private AtomicConstraint getConstraint(List<AtomicConstraint> identicalAtomicConstraints) {
        return identicalAtomicConstraints.iterator().next();
    }

    private boolean decisionIsFactorisable(DecisionNode decision, AtomicConstraint factorisingConstraint, AtomicConstraint negatedFactorisingConstraint){
        // The decision should contain ONE option with the MPC
        boolean optionWithMPCExists = decision.getOptions().stream()
            .filter(option -> option.atomicConstraintExists(factorisingConstraint))
            .count() == 1;

        // The decision should contain ONE separate option with the negated MPC (which is atomic).
        boolean optionWithNegatedMPCExists = decision.getOptions().stream()
            .filter(option -> option.atomicConstraintExists(negatedFactorisingConstraint) && option.getAtomicConstraints().size() == 1)
            .count() == 1;

        return optionWithMPCExists && optionWithNegatedMPCExists;
    }

    class DecisionAnalyser {
        private DecisionNode decision;
        private AtomicConstraint factorisingConstraint;
        private AtomicConstraint negatedFactorisingConstraint;
        private Set<AtomicConstraint> atomicConstraintsAssociatedWithFactorisingOption = new HashSet<>();
        private Set<AtomicConstraint> atomicConstraintsAssociatedWithNegatedOption = new HashSet<>();

        DecisionAnalyser(DecisionNode decisionNode, AtomicConstraint factorisingConstraint){
            this.decision = decisionNode;
            this.factorisingConstraint = factorisingConstraint;
            this.negatedFactorisingConstraint = factorisingConstraint.negate();
        }

        /**
         * Iterate through a decision nodes options and determine whether factorisation is possible
         */
        DecisionAnalysisResult performAnalysis() {
            DecisionAnalysisResult result = new DecisionAnalysisResult();
            List<ConstraintNode> otherOptions = new ArrayList<>();
            for (ConstraintNode option : decision.getOptions()) {
                boolean optionContainsProlificConstraint = option.atomicConstraintExists(factorisingConstraint);
                boolean optionContainsNegatedProlificConstraint = option.atomicConstraintExists(negatedFactorisingConstraint);
                if (optionContainsProlificConstraint && optionContainsNegatedProlificConstraint) {
                    throw new RuntimeException("Contradictory constraint node");
                } else if (optionContainsProlificConstraint) {
                    markOptionForFactorisation(factorisingConstraint, option, result.optionsToFactorise, atomicConstraintsAssociatedWithFactorisingOption);
                } else if (optionContainsNegatedProlificConstraint) {
                    markOptionForFactorisation(negatedFactorisingConstraint, option, result.negatedOptionsToFactorise, atomicConstraintsAssociatedWithNegatedOption);
                } else {
                    // This option does not contain the factorising constraint so add to a separate list.
                    otherOptions.add(option);
                }
            }

            // The following options need moving either to:
            // * an option under the factorising constraint node,
            // * an option under the negated factorising constraint node,
            // * or another option alongside the factorising constraint node
            for (ConstraintNode option : otherOptions) {
                boolean nodeCanBeMovedUnderFactorised = constraintNodeContainsNegatedConstraints(option, atomicConstraintsAssociatedWithFactorisingOption);
                boolean nodeCanBeMovedUnderNegatedFactorised = constraintNodeContainsNegatedConstraints(option, atomicConstraintsAssociatedWithNegatedOption);
                if (nodeCanBeMovedUnderFactorised) {
                    result.optionsToFactorise.add(option);
                } else if (nodeCanBeMovedUnderNegatedFactorised) {
                    result.negatedOptionsToFactorise.add(option);
                } else {
                    result.adjacentOptions.add(option);
                }
            }
            return result;
        }

        private void markOptionForFactorisation(AtomicConstraint factorisingConstraint, ConstraintNode node, List<ConstraintNode> options, Set<AtomicConstraint> constraints){
            ConstraintNode newOption = node.cloneWithoutAtomicConstraint(factorisingConstraint);
            if (!newOption.getAtomicConstraints().isEmpty()){
                options.add(newOption);
                constraints.addAll(newOption.getAtomicConstraints());
            }
        }
    }

    class DecisionAnalysisResult {
        List<ConstraintNode> optionsToFactorise = new ArrayList<>();
        List<ConstraintNode> negatedOptionsToFactorise = new ArrayList<>();
        List<ConstraintNode> adjacentOptions = new ArrayList<>();
    }
}
