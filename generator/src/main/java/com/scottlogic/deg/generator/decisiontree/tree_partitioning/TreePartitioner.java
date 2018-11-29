package com.scottlogic.deg.generator.decisiontree.tree_partitioning;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;

import java.util.stream.Stream;

public interface TreePartitioner {
    Stream<DecisionTree> splitTreeIntoPartitions(DecisionTree decisionTree);
}
