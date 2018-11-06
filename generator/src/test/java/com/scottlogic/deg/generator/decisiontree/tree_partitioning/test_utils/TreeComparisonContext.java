package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;

import java.util.ArrayList;

public class TreeComparisonContext {
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

    public void reportOptionDifferences(ArrayList missingExpectedOptions, ArrayList missingActualOptions) {
        if (reportError != null)
            return;

        reportError = () -> {
            if (!missingExpectedOptions.isEmpty()) {
                System.out.println(String.format("Got Option %s", missingExpectedOptions));
            }
            if (!missingActualOptions.isEmpty()) {
                System.out.println(String.format("Expected Option %s", missingActualOptions));
            }
        };
    }

    public void reportDecisionDifferences(ArrayList missingExpectedDecisions, ArrayList missingActualDecisions) {
        if (reportError != null)
            return;

        reportError = () -> {
            if (!missingExpectedDecisions.isEmpty()) {
                System.out.println(String.format("Got Decision %s", missingExpectedDecisions));
            }
            if (!missingActualDecisions.isEmpty()) {
                System.out.println(String.format("Expected Decision %s", missingActualDecisions));
            }
        };
    }

    public void reportAtomicConstraintDifferences(
        ArrayList missingExpectedAtomicConstraints,
        ArrayList missingActualAtomicConstraints) {
        if (reportError != null)
            return;

        reportError = () -> {
            if (!missingExpectedAtomicConstraints.isEmpty()) {
                System.out.println(String.format("Got Atomic Constraint %s", missingExpectedAtomicConstraints));
            }
            if (!missingActualAtomicConstraints.isEmpty()) {
                System.out.println(String.format("Expected Atomic Constraint %s", missingActualAtomicConstraints));
            }
        };
    }
}
