package com.scottlogic.deg.generator.decisiontree;

import java.util.stream.Stream;

public interface ITreePartitioner {
    Stream<DecisionTree> splitTreeIntoPartitions(DecisionTree decisionTree);
}
