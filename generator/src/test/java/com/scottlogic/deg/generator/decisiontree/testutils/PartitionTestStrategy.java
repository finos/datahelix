package com.scottlogic.deg.generator.decisiontree.testutils;

import java.util.List;
import java.util.stream.Collectors;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.RelatedFieldTreePartitioner;
import com.scottlogic.deg.generator.decisiontree.treepartitioning.TreePartitioner;

public class PartitionTestStrategy implements TreeTransformationTestStrategy {
    @Override
    public String getTestsDirName() {
        return "partitioning-tests";
    }

    @Override
    public List<DecisionTree> transformTree(DecisionTree beforeTree) {
        TreePartitioner treePartitioner = new RelatedFieldTreePartitioner();
        return treePartitioner
                .splitTreeIntoPartitions(beforeTree)
                .collect(Collectors.toList());        
    }
    
    

}
