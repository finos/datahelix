package com.scottlogic.deg.generator.decisiontree;

import java.util.stream.Stream;

public class NoopTreePartitioner implements ITreePartitioner {
    @Override
    public Stream<DecisionTree> splitTreeIntoPartitions(DecisionTree decisionTree) {
        return Stream.of(decisionTree);
    }
}
