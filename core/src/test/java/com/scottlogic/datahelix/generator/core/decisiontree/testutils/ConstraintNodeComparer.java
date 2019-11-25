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

package com.scottlogic.datahelix.generator.core.decisiontree.testutils;

import com.scottlogic.datahelix.generator.core.decisiontree.ConstraintNode;
import com.scottlogic.datahelix.generator.core.decisiontree.DecisionNode;

import java.util.Collection;

public class ConstraintNodeComparer implements EqualityComparer {
    private final EqualityComparer atomicConstraintAnyOrderComparer;
    private final EqualityComparer decisionComparer;
    private final AnyOrderCollectionEqualityComparer decisionAnyOrderComparer;
    private final TreeComparisonContext comparisonContext;
    private final CollectionEqualityComparer atomicConstraintCollectionEqualityComparer;
    private final CollectionEqualityComparer decisionCollectionEqualityComparer;

    public ConstraintNodeComparer(
        TreeComparisonContext comparisonContext,
        EqualityComparer atomicConstraintAnyOrderComparer,
        EqualityComparer decisionComparer,
        CollectionEqualityComparer atomicConstraintCollectionEqualityComparer,
        CollectionEqualityComparer decisionCollectionEqualityComparer) {
    this.comparisonContext = comparisonContext;
        this.decisionComparer = decisionComparer;
        this.decisionAnyOrderComparer = new AnyOrderCollectionEqualityComparer(decisionComparer);
        this.atomicConstraintAnyOrderComparer = atomicConstraintAnyOrderComparer;
        this.atomicConstraintCollectionEqualityComparer = atomicConstraintCollectionEqualityComparer;
        this.decisionCollectionEqualityComparer = decisionCollectionEqualityComparer;
    }

    @Override
    public int getHashCode(Object item) {
        return getHashCode((ConstraintNode)item);
    }

    public int getHashCode(ConstraintNode constraint) {
        int decisionsHashCode = constraint
            .getDecisions()
            .stream()
            .reduce(
                0,
                (prev, decision) -> prev * decisionComparer.getHashCode(decision),
                (prevHash, decisionHash) -> prevHash * decisionHash);

        int atomicConstraintsHashCode = constraint
            .getAtomicConstraints()
            .stream()
            .reduce(
                0,
                (prev, atomicConstraint) -> prev * atomicConstraint.hashCode(),
                (prevHash, atomicConstraintHash) -> prevHash * atomicConstraintHash);

        return decisionsHashCode * atomicConstraintsHashCode;
    }

    @Override
    public boolean equals(Object item1, Object item2) {
        return equals((ConstraintNode)item1, (ConstraintNode)item2);
    }

    public boolean equals(ConstraintNode constraint1, ConstraintNode constraint2) {
        try {
            comparisonContext.pushToStack(constraint1, constraint2);

            if (!atomicConstraintsMatch(constraint1, constraint2)) {
                comparisonContext.reportDifferences(
                    atomicConstraintCollectionEqualityComparer.getItemsMissingFrom(
                        constraint1.getAtomicConstraints(),
                        constraint2.getAtomicConstraints()
                    ),
                    atomicConstraintCollectionEqualityComparer.getItemsMissingFrom(
                        constraint2.getAtomicConstraints(),
                        constraint1.getAtomicConstraints()
                    ),
                    TreeComparisonContext.TreeElementType.ATOMIC_CONSTRAINT);
                return false;
            }

            boolean decisionsMatch = decisionAnyOrderComparer.equals(constraint1.getDecisions(), constraint2.getDecisions());
            if (!decisionsMatch) {
                comparisonContext.reportDifferences(
                    decisionCollectionEqualityComparer.getItemsMissingFrom(
                        constraint1.getDecisions(),
                        constraint2.getDecisions()
                    ),
                    decisionCollectionEqualityComparer.getItemsMissingFrom(
                        constraint2.getDecisions(),
                        constraint1.getDecisions()
                    ),
                    TreeComparisonContext.TreeElementType.DECISION);
                return false;
            }

            for (DecisionNode constraint1Decision : constraint1.getDecisions()) {
                DecisionNode constraint2Decision = getDecision(constraint1Decision, constraint2.getDecisions());

                if (!optionsAreEqual(constraint1Decision, constraint2Decision))
                    return false;
            }

            return true;
        }
        finally {
            comparisonContext.popFromStack();
        }
    }

    private boolean atomicConstraintsMatch(ConstraintNode constraint1, ConstraintNode constraint2) {
        return atomicConstraintAnyOrderComparer.equals(constraint1.getAtomicConstraints(), constraint2.getAtomicConstraints());
    }

    private DecisionNode getDecision(DecisionNode toFind, Collection<DecisionNode> decisions) {
        return decisions
            .stream()
            .filter(c -> decisionComparer.equals(c, toFind))
            .findFirst()
            .orElse(null);
    }

    private boolean optionsAreEqual(DecisionNode decision1, DecisionNode decision2) {
        try{
            comparisonContext.pushToStack(decision1, decision2);

            for (ConstraintNode option1: decision1.getOptions()){
                ConstraintNode option2 = getOption(option1, decision2.getOptions());

                if (!this.equals(option1, option2)){
                    return false;
                }
            }

            return true;
        }
        finally {
            comparisonContext.popFromStack();
        }
    }

    private ConstraintNode getOption(ConstraintNode toFind, Collection<ConstraintNode> constraints) {
        return constraints
            .stream()
            .filter(c -> atomicConstraintsMatch(toFind, c))
            .findFirst()
            .orElse(null);
    }
}
