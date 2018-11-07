package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;

import java.util.ArrayList;

public class TreeComparisonContext {
    public DecisionTree leftTree;
    public DecisionTree rightTree;

    public ConstraintNode left;
    public ConstraintNode right;
    public final ArrayList leftPath = new ArrayList();
    public final ArrayList rightPath = new ArrayList();
    public Runnable reportError = null;

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

    public void reportDecisionDifferences(ArrayList missingExpectedDecisions, ArrayList missingActualDecisions) {
        if (reportError != null)
            reportError.run();

        reportError = () -> {
            if (!missingExpectedDecisions.isEmpty()) {
                System.out.println(String.format("%s: Got Decision %s", rightTree.getDescription(), missingExpectedDecisions));
            }
            if (!missingActualDecisions.isEmpty()) {
                System.out.println(String.format("%s: Expected Decision %s", leftTree.getDescription(), missingActualDecisions));
            }
        };
    }

    public void reportAtomicConstraintDifferences(
        ArrayList missingExpectedAtomicConstraints,
        ArrayList missingActualAtomicConstraints) {
        if (reportError != null)
            reportError.run();

        reportError = () -> {
            if (!missingExpectedAtomicConstraints.isEmpty()) {
                System.out.println(String.format("%s: Got Atomic Constraint %s", rightTree.getDescription(), missingExpectedAtomicConstraints));
            }
            if (!missingActualAtomicConstraints.isEmpty()) {
                System.out.println(String.format("%s: Expected Atomic Constraint %s", leftTree.getDescription(), missingActualAtomicConstraints));
            }
        };
    }

    public void setTrees(DecisionTree left, DecisionTree right) {
        this.leftTree = left;
        this.rightTree = right;
    }
}
