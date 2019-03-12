package com.scottlogic.deg.generator.decisiontree.treepartitioning;

import com.scottlogic.deg.generator.decisiontree.DecisionTree;

import java.util.stream.Stream;

public class NoopTreePartitioner implements TreePartitioner {
    @Override
    public Stream<DecisionTree> splitTreeIntoPartitions(DecisionTree decisionTree) {
        return Stream.of(decisionTree);
    }
}
