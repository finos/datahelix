package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;

import java.util.ArrayList;
import java.util.Objects;

public class TreeComparisonContext {
    public ConstraintNode left;
    public ConstraintNode right;
    public final ArrayList leftPath = new ArrayList();
    public final ArrayList rightPath = new ArrayList();

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

    public void reset() {
        this.leftPath.clear();
        this.rightPath.clear();
        this.left = null;
        this.right = null;
    }

    public String getLeftPath(){
        return Objects.toString(this.left);
    }

    public String getRightPath(){
        return Objects.toString(this.right);
    }
}
