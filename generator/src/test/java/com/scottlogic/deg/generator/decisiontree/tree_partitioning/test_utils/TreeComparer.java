package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;

public class TreeComparer implements IEqualityComparer {
    @Override
    public int getHashCode(Object item) {
        return 0; //how to calculate a hashCode for a tree!
    }

    @Override
    public boolean equals(Object item1, Object item2) {
        return equals((DecisionTree)item1, (DecisionTree)item2);
    }

    public boolean equals(DecisionTree tree1, DecisionTree tree2) {
        if (tree1 == null && tree2 == null)
            return true;

        if (tree1 == null || tree2 == null)
            return false; //either tree1 XOR tree2 is null

        IEqualityComparer constraintComparer = new ConstraintNodeComparer();
        return constraintComparer.equals(tree1.getRootNode(), tree2.getRootNode());
    }
}
