package com.scottlogic.deg.generator.decisiontree.tree_partitioning;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;

import java.util.stream.Stream;

public interface ITreePartitioner {
    Stream<DecisionTree> splitTreeIntoPartitions(DecisionTree decisionTree);
}
