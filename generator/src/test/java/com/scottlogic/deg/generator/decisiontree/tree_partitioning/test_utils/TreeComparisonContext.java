package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;

import java.util.ArrayList;
import java.util.Collection;

public class TreeComparisonContext {
    public DecisionTree expectedTree;
    public DecisionTree actualTree;

    public ConstraintNode left;
    public ConstraintNode right;
    public final ArrayList leftPath = new ArrayList();
    public final ArrayList rightPath = new ArrayList();
    private final ArrayList<Error> errors = new ArrayList<>();

    class Error {
        public final ErrorContext expected;
        public final ErrorContext actual;
        public final TreeElementType type;

        public Error(ErrorContext expected, ErrorContext actual, TreeElementType type) {
            this.expected = expected;
            this.actual = actual;
            this.type = type;
        }
    }

    class ErrorContext {
        public final DecisionTree tree;
        public final Object value;

        public ErrorContext(DecisionTree tree, Object value) {
            this.tree = tree;
            this.value = value;
        }
    }

    public enum TreeElementType {
        Decision,
        AtomicConstraint
    }

    public TreeComparisonContext() {
        this.left = null;
        this.right = null;
    }

    public void setConstraint(ConstraintNode left, ConstraintNode right) {
        this.leftPath.add(left);
        this.rightPath.add(right);
        this.left = left;
        this.right = right;
    }

    public void setDecision(DecisionNode left, DecisionNode right) {
        this.leftPath.add(left);
        this.rightPath.add(right);
    }

    public void setTrees(DecisionTree expectedTree, DecisionTree actualTree) {
        this.expectedTree = expectedTree;
        this.actualTree = actualTree;
    }

    public Collection<Error> getErrors(){
        return this.errors;
    }

    public void reportDecisionDifferences(ArrayList missingExpectedDecisions, ArrayList missingActualDecisions) {
        errors.add(new Error(
            new ErrorContext(expectedTree, missingExpectedDecisions),
            new ErrorContext(actualTree, missingActualDecisions),
            TreeElementType.Decision));
    }

    public void reportAtomicConstraintDifferences(
        ArrayList missingExpectedAtomicConstraints,
        ArrayList missingActualAtomicConstraints) {
        errors.add(new Error(
            new ErrorContext(expectedTree, missingExpectedAtomicConstraints),
            new ErrorContext(actualTree, missingActualAtomicConstraints),
            TreeElementType.AtomicConstraint));
    }
}

