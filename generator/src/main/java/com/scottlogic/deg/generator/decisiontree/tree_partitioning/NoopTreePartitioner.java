package com.scottlogic.deg.generator.decisiontree.tree_partitioning;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;

import java.util.stream.Stream;

public class NoopTreePartitioner implements ITreePartitioner {
    @Override
    public Stream<DecisionTree> splitTreeIntoPartitions(DecisionTree decisionTree) {
        return Stream.of(decisionTree);
    }
}
