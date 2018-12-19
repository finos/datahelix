package com.scottlogic.deg.generator.decisiontree.test_utils;

import java.util.List;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.ConstraintToFieldMapper;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.RelatedFieldTreePartitioner;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.TreePartitioner;

public class PartitionTestStrategy implements TreeTransformationTestStrategy {
    private final ConstraintToFieldMapper fieldMapper = new ConstraintToFieldMapper();

    @Override
    public String getTestsDirName() {
        return "partitioning-tests";
    }

    @Override
    public List<DecisionTree> transformTree(DecisionTree beforeTree) {
        TreePartitioner treePartitioner = new RelatedFieldTreePartitioner(fieldMapper);
        return treePartitioner
                .splitTreeIntoPartitions(beforeTree)
                .collect(Collectors.toList());        
    }
    
    

}
