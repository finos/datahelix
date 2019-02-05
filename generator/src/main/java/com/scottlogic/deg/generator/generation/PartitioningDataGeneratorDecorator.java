package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.PartitionCombiner;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.TreePartitioner;
import com.scottlogic.deg.generator.outputs.GeneratedObject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PartitioningDataGeneratorDecorator implements DataGenerator {

    private final DataGenerator underlying;
    private final TreePartitioner treePartitioner;
    private final PartitionCombiner partitionCombiner;

    public PartitioningDataGeneratorDecorator(DataGenerator underlying, TreePartitioner treePartitioner, PartitionCombiner partitionCombiner){
        this.underlying = underlying;
        this.treePartitioner = treePartitioner;
        this.partitionCombiner = partitionCombiner;
    }

    @Override
    public Stream<GeneratedObject> generateData(Profile profile, DecisionTree analysedProfile, GenerationConfig generationConfig) {
        Stream<DecisionTree> treeStream = treePartitioner.splitTreeIntoPartitions(analysedProfile);

        List<Stream<GeneratedObject>> partitionedRows = treeStream
            .map(tree -> underlying.generateData(profile, tree, generationConfig))
            .collect(Collectors.toList());

        return partitionCombiner.combine(partitionedRows);
    }
}
