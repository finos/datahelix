package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;

public class TreeComparisonContext {
    public ConstraintNode left;
    public ConstraintNode right;
    public final ArrayList leftPath = new ArrayList();
    public final ArrayList rightPath = new ArrayList();
    public boolean errorReported;
    public BiConsumer<Collection, Collection> reportErrors;

    public TreeComparisonContext() {
        this.left = null;
        this.right = null;
    }

    private TreeComparisonContext(ConstraintNode left, ConstraintNode right) {
        this.left = left;
        this.right = right;
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

    public String getLeftPath(){
        return Objects.toString(this.left);
    }

    public String getRightPath(){
        return Objects.toString(this.right);
    }

    public void reportOptionDifferences(ArrayList missingExpectedOptions, ArrayList missingActualOptions) {
        if (errorReported)
            return;
        errorReported = true;

        if (!missingExpectedOptions.isEmpty()) {
            System.out.println(String.format("Got Option %s", missingExpectedOptions));
        }
        if (!missingActualOptions.isEmpty()) {
            System.out.println(String.format("Expected Option %s", missingActualOptions));
        }
    }

    public void reportDecisionDifferences(ArrayList missingExpectedDecisions, ArrayList missingActualDecisions) {
        if (errorReported)
            return;
        errorReported = true;

        if (!missingExpectedDecisions.isEmpty()) {
            System.out.println(String.format("Got Decision %s", missingExpectedDecisions));
        }
        if (!missingActualDecisions.isEmpty()) {
            System.out.println(String.format("Expected Decision %s", missingActualDecisions));
        }
    }

    public void reportAtomicConstraintDifferences(
        ArrayList missingExpectedAtomicConstraints,
        ArrayList missingActualAtomicConstraints) {
        if (errorReported)
            return;
        errorReported = true;

        if (!missingExpectedAtomicConstraints.isEmpty()) {
            System.out.println(String.format("Got Atomic Constraint %s", missingExpectedAtomicConstraints));
        }
        if (!missingActualAtomicConstraints.isEmpty()) {
            System.out.println(String.format("Expected Atomic Constraint %s", missingActualAtomicConstraints));
        }
    }
}
