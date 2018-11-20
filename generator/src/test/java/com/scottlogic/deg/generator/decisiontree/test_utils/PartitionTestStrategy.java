package com.scottlogic.deg.generator.decisiontree.test_utils;

import java.util.List;
import java.util.stream.Collectors;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.StandardTreePartitioner;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.TreePartitioner;

public class PartitionTestStrategy implements TreeTransformationTestStrategy {
    @Override
    public String getTestsDirName() {
        return "partitioning-tests";
    }

    @Override
    public List<DecisionTree> transformTree(DecisionTree beforeTree) {
        TreePartitioner treePartitioner = new StandardTreePartitioner();
        return treePartitioner
                .splitTreeIntoPartitions(beforeTree)
                .collect(Collectors.toList());        
    }
    
    

}
