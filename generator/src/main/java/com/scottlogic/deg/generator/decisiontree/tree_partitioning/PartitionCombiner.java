package com.scottlogic.deg.generator.decisiontree.tree_partitioning;

import com.scottlogic.deg.generator.outputs.GeneratedObject;

import java.util.List;
import java.util.stream.Stream;

public interface PartitionCombiner {
    Stream<GeneratedObject> combine(List<Stream<GeneratedObject>> dataBagStreams);
}
